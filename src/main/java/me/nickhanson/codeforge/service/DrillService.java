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
 * Service for managing drill items and recording outcomes.
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
     * Retrieves a list of due drill items for the specified user, up to the given limit.
     * If no items are currently due, returns the soonest upcoming item if available.
     *
     * @param limit  the maximum number of drill items to retrieve
     * @param userId the ID of the user
     * @return a list of due drill items or the soonest upcoming item
     * @throws IllegalArgumentException if limit is less than or equal to zero or if userId is null/blank
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
     * Records the outcome of a user's attempt at a challenge and updates the corresponding drill item.
     *
     * @param challengeId the ID of the challenge
     * @param outcome     the outcome of the attempt
     * @param code        the code submitted by the user
     * @param userId      the ID of the user
     * @return the recorded submission
     * @throws IllegalArgumentException if userId is null/blank or if the challenge is not found
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
     * Retrieves an existing DrillItem for the given challenge and user, or creates a new one if none exists.
     *
     * @param challenge the challenge associated with the drill item
     * @param userId    the ID of the user
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
     * Computes the next due date for a drill item based on the outcome and current streak.
     *
     * @param outcome   the outcome of the user's attempt
     * @param newStreak the updated streak after the attempt
     * @return the computed next due date as an Instant
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
     * Checks if a user is enrolled in a drill for a specific challenge.
     *
     * @param challengeId the ID of the challenge
     * @param userId      the ID of the user
     * @return true if the user is enrolled in the drill, false otherwise
     */
    public boolean isEnrolledInDrill(Long challengeId, String userId) {
        return !drillItemDao.listByChallengeIdAndUser(challengeId, userId).isEmpty();
    }

    /**
     * Ensures that a DrillItem exists for the given challenge and user, creating one if necessary.
     *
     * @param challengeId the ID of the challenge
     * @param userId      the ID of the user
     * @return the existing or newly created DrillItem
     * @throws IllegalArgumentException if the challenge is not found
     */
    public DrillItem ensureDrillItem(Long challengeId, String userId) {
        Challenge challenge = Optional.ofNullable(challengeDao.getById(challengeId))
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + challengeId));
        return getOrCreateDrillItem(challenge, userId);
    }

    /**
     * Retrieves the next due DrillItem for the specified user.
     *
     * @param userId the ID of the user
     * @return an Optional containing the next due DrillItem, or empty if none are due
     */
    public Optional<DrillItem> nextDue(String userId) {
        List<DrillItem> queue = getDueQueue(1, userId);
        return queue.isEmpty() ? Optional.empty() : Optional.of(queue.get(0));
    }

    /**
     * Ensures that DrillItems exist for a list of challenges for the specified user.
     *
     * @param challenges the list of challenges
     * @param userId     the ID of the user
     * @return the number of DrillItems created
     * @throws IllegalArgumentException if userId is null/blank
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