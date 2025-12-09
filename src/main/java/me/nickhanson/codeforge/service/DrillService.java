package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.*;
import me.nickhanson.codeforge.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service that encapsulates drill scheduling, queue orchestration, and
 * updating DrillItem metrics (timesSeen, streak, nextDueAt) when a user submits an outcome.
 * @author nickhanson
 */
public class DrillService {
    private static final Logger log = LogManager.getLogger(DrillService.class);

    private final DrillItemDao drillItemDao;
    private final SubmissionDao submissionDao;
    private final ChallengeDao challengeDao;

    // Default constructor initializing DAOs
    public DrillService() {
        this.drillItemDao = new DrillItemDao();
        this.submissionDao = new SubmissionDao();
        this.challengeDao = new ChallengeDao();
    }

    // Constructor for DI (e.g., for testing)
    public DrillService(DrillItemDao drillItemDao, SubmissionDao submissionDao, ChallengeDao challengeDao) {
        this.drillItemDao = drillItemDao;
        this.submissionDao = submissionDao;
        this.challengeDao = challengeDao;
    }

    /**
     * Retrieves up to 'limit' DrillItems that are due for review.
     * If none are due, returns the soonest upcoming DrillItem (if any).
     * @param limit maximum number of DrillItems to retrieve
     * @return list of due DrillItems
     */
    public List<DrillItem> getDueQueue(int limit, String userId) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be > 0");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId required");
        }
        Instant now = Instant.now();
        List<DrillItem> due = drillItemDao.dueQueue(now, limit, userId);
        if (!due.isEmpty()) {
            return due;
        }
        Optional<DrillItem> soonest = drillItemDao.soonestUpcoming(userId);
        return soonest.map(List::of).orElseGet(List::of);
    }

    /**
     * Records the outcome of a user's attempt at a challenge, updating the corresponding DrillItem metrics.
     * @param challengeId ID of the challenge
     * @param outcome outcome of the attempt
     * @param code user's submitted code
     * @return the recorded Submission
     */
    public Submission recordOutcome(Long challengeId, Outcome outcome, String code, String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId required");
        }
        Challenge challenge = Optional.ofNullable(challengeDao.getById(challengeId))
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + challengeId));

        DrillItem drillItem = getOrCreateDrillItem(challenge, userId);

        // Persist submission first for audit trail
        Submission submission = new Submission(challenge, outcome, code);
        submission.setUserId(userId);
        submissionDao.saveOrUpdate(submission);

        // Update DrillItem metrics
        drillItem.setTimesSeen(drillItem.getTimesSeen() + 1);
        int currentStreak = drillItem.getStreak();
        int newStreak = switch (outcome) {
            case CORRECT, ACCEPTABLE -> currentStreak + 1;
            case INCORRECT -> 0;
            case SKIPPED -> currentStreak;
        };
        drillItem.setStreak(newStreak);
        drillItem.setNextDueAt(computeNextDueAt(outcome, newStreak));

        drillItemDao.saveOrUpdate(drillItem);
        log.debug("Updated DrillItem userId={} challengeId={} timesSeen={} streak={} nextDueAt={} after outcome {}",
                userId, challengeId, drillItem.getTimesSeen(), drillItem.getStreak(), drillItem.getNextDueAt(), outcome);

        return submission;
    }

    /**
     * Retrieves the DrillItem for the given Challenge, creating it if it doesn't exist.
     * @param challenge the Challenge
     * @return the existing or newly created DrillItem
     */
    private DrillItem getOrCreateDrillItem(Challenge challenge, String userId) {
        List<DrillItem> items = drillItemDao.listByChallengeIdAndUser(challenge.getId(), userId);
        if (!items.isEmpty()) return items.get(0);
        DrillItem created = new DrillItem(challenge);
        created.setUserId(userId);
        drillItemDao.saveOrUpdate(created);
        return created;
    }

    /**
     * Computes the next due time for a DrillItem based on the outcome and new streak.
     * @param outcome the outcome of the attempt
     * @param newStreak the updated streak count
     * @return the computed next due Instant
     */
    private Instant computeNextDueAt(Outcome outcome, int newStreak) {
        Instant now = Instant.now();
        return switch (outcome) {
            case INCORRECT -> now.plus(Duration.ofMinutes(5));
            case SKIPPED -> now.plus(Duration.ofMinutes(10));
            case CORRECT, ACCEPTABLE -> {
                if (newStreak <= 1) yield now.plus(Duration.ofMinutes(30));
                if (newStreak == 2) yield now.plus(Duration.ofDays(1));
                if (newStreak == 3) yield now.plus(Duration.ofDays(3));
                yield now.plus(Duration.ofDays(7));
            }
        };
    }

    /**
     * Checks if a user is enrolled in drill for a specific challenge.
     * @param challengeId ID of the challenge
     * @return true if enrolled, false otherwise
     */
    public boolean isEnrolledInDrill(Long challengeId, String userId) {
        return !drillItemDao.listByChallengeIdAndUser(challengeId, userId).isEmpty();
    }

    /**
     * Ensures a DrillItem exists for the given challenge, creating it if necessary.
     * @param challengeId ID of the challenge
     * @return the existing or newly created DrillItem
     */
    public DrillItem ensureDrillItem(Long challengeId, String userId) {
        Challenge challenge = Optional.ofNullable(challengeDao.getById(challengeId))
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + challengeId));
        return getOrCreateDrillItem(challenge, userId);
    }

    /**
     * Retrieves the next due DrillItem, if any.
     * @return an Optional containing the next due DrillItem, or empty if none exist
     */
    public Optional<DrillItem> nextDue(String userId) {
        List<DrillItem> queue = getDueQueue(1, userId);
        return queue.isEmpty() ? Optional.empty() : Optional.of(queue.get(0));
    }

    /**
     * Ensures DrillItems exist for the given user across the provided challenges.
     * Creates missing items and leaves existing ones untouched.
     * @return number of DrillItems created
     */
    public int ensureEnrollmentForUser(List<Challenge> challenges, String userId) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId required");
        int created = 0;
        for (Challenge ch : challenges) {
            List<DrillItem> items = drillItemDao.listByChallengeIdAndUser(ch.getId(), userId);
            if (items.isEmpty()) {
                DrillItem di = new DrillItem(ch);
                di.setUserId(userId);
                drillItemDao.saveOrUpdate(di);
                created++;
            }
        }
        return created;
    }
}