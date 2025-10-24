package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.*;
import me.nickhanson.codeforge.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service that encapsulates drill scheduling, queue orchestration, and
 * updating DrillItem metrics (timesSeen, streak, nextDueAt) when a user submits an outcome.
 */
@Service
public class DrillService {
    private static final Logger log = LoggerFactory.getLogger(DrillService.class);

    private final DrillItemDao drillItemDao;
    private final SubmissionDao submissionDao;
    private final ChallengeDao challengeDao;

    /**
     * Constructs a DrillService with the necessary DAOs.
     *
     * @param drillItemDao the DrillItem DAO
     * @param submissionDao the Submission DAO
     * @param challengeDao the Challenge DAO
     */
    public DrillService(DrillItemDao drillItemDao, SubmissionDao submissionDao, ChallengeDao challengeDao) {
        this.drillItemDao = drillItemDao;
        this.submissionDao = submissionDao;
        this.challengeDao = challengeDao;
    }

    /**
     * Returns a queue of due DrillItems.
     * - First preference: items with nextDueAt == null or <= now, sorted by nextDueAt asc (nulls first).
     * - If none are due, return the single soonest upcoming item (min nextDueAt) to avoid an empty queue.
     *
     * @param limit Max number of items to return (must be > 0)
     * @return List of due DrillItems, up to the specified limit
     * @throws IllegalArgumentException if limit <= 0
     */
    @Transactional(readOnly = true)
    public List<DrillItem> getDueQueue(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("limit must be > 0");

        Instant now = Instant.now();
        List<DrillItem> all = drillItemDao.findAll();
        List<DrillItem> due = all.stream()
                .filter(di -> di.getNextDueAt() == null || !di.getNextDueAt().isAfter(now))
                .sorted(Comparator.comparing(DrillItem::getNextDueAt, Comparator.nullsFirst(Comparator.naturalOrder())))
                .limit(limit)
                .collect(Collectors.toList());

        if (!due.isEmpty()) {
            return due;
        }
        // No items due; return soonest one item to prevent empty queue UX
        return all.stream()
                .filter(di -> di.getNextDueAt() != null)
                .min(Comparator.comparing(DrillItem::getNextDueAt))
                .map(List::of)
                .orElse(List.of());
    }

    /**
     * Records a submission outcome for a challenge and updates its DrillItem scheduling.
     * Creates the DrillItem if not present.
     * Scheduling rules (simple spaced repetition v1):
     * - CORRECT/ACCEPTABLE: increment streak; schedule based on new streak:
     *   streak 1 -> +30 minutes, 2 -> +1 day, 3 -> +3 days, 4+ -> +7 days
     * - INCORRECT: reset streak to 0; schedule to +5 minutes
     * - SKIPPED: keep streak as-is (no increment); schedule to +10 minutes
     * timesSeen always increments.
     *
     * @param challengeId the ID of the Challenge
     * @param outcome the outcome of the submission
     * @param code the submitted code
     * @return the persisted Submission entity
     * @throws IllegalArgumentException if the Challenge does not exist
     */
    @Transactional
    public Submission recordOutcome(Long challengeId, Outcome outcome, String code) {
        Challenge challenge = challengeDao.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + challengeId));

        DrillItem drillItem = getOrCreateDrillItem(challenge);

        // Persist submission first for audit trail
        Submission submission = new Submission(challenge, outcome, code);
        submissionDao.save(submission);

        // Update DrillItem metrics
        drillItem.setTimesSeen(drillItem.getTimesSeen() + 1);
        int newStreak = drillItem.getStreak();
        switch (outcome) {
            case CORRECT, ACCEPTABLE -> newStreak = drillItem.getStreak() + 1;
            case INCORRECT -> newStreak = 0;
            case SKIPPED -> newStreak = drillItem.getStreak();
        }
        drillItem.setStreak(newStreak);
        drillItem.setNextDueAt(computeNextDueAt(outcome, newStreak));

        DrillItem saved = drillItemDao.save(drillItem);
        log.debug("Updated DrillItem id={} challengeId={} timesSeen={} streak={} nextDueAt={} after outcome {}",
                saved.getId(), challengeId, saved.getTimesSeen(), saved.getStreak(), saved.getNextDueAt(), outcome);

        return submission;
    }

    /**
     * Retrieves the DrillItem for the given Challenge, creating it if it doesn't exist.
     *
     * @param challenge the Challenge entity
     * @return the existing or newly created DrillItem
     */
    private DrillItem getOrCreateDrillItem(Challenge challenge) {
        List<DrillItem> items = drillItemDao.findByChallengeId(challenge.getId());
        if (!items.isEmpty()) return items.get(0);
        DrillItem created = new DrillItem(challenge);
        return drillItemDao.save(created);
    }

    /**
     * Computes the next due time for a DrillItem based on the submission outcome and new streak.
     *
     * @param outcome the outcome of the submission
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
     * Ensures a DrillItem exists for the given challengeId, creating it if necessary.
     *
     * @param challengeId the ID of the Challenge
     * @return the existing or newly created DrillItem
     * @throws IllegalArgumentException if the Challenge does not exist
     */
    @Transactional
    public DrillItem ensureDrillItem(Long challengeId) {
        Challenge c = challengeDao.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found: " + challengeId));
        return getOrCreateDrillItem(c);
    }

    /**
     * Retrieves the next due DrillItem, if any.
     *
     * @return an Optional containing the next due DrillItem, or empty if none are due
     */
    @Transactional(readOnly = true)
    public Optional<DrillItem> nextDue() {
        List<DrillItem> q = getDueQueue(1);
        return q.isEmpty() ? Optional.empty() : Optional.of(q.get(0));
    }
}

