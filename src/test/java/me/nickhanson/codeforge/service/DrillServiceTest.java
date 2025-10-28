package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.*;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.persistence.DrillItemDao;
import me.nickhanson.codeforge.persistence.SubmissionDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({DrillService.class, ChallengeDao.class, DrillItemDao.class, SubmissionDao.class})
class DrillServiceTest {

    @Autowired
    ChallengeDao challengeDao;
    @Autowired
    DrillItemDao drillItemDao;
    @Autowired
    DrillService drillService;

    private Challenge newChallenge(String title) {
        return challengeDao.save(new Challenge(title, Difficulty.EASY, "blurb", "prompt"));
    }

    @Test
    void recordOutcome_createsItem_and_updates_streak_timesSeen_and_schedule_for_correct() {
        Challenge ch = newChallenge("Two Sum");
        Instant before = Instant.now();
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, "code");

        List<DrillItem> items = drillItemDao.findByChallengeId(ch.getId());
        assertThat(items).hasSize(1);
        DrillItem di = items.get(0);
        assertThat(di.getTimesSeen()).isEqualTo(1);
        assertThat(di.getStreak()).isEqualTo(1);
        assertThat(di.getNextDueAt()).isNotNull();

        Duration until = Duration.between(before, di.getNextDueAt());
        // Expect around 30 minutes for streak 1
        assertThat(until).isGreaterThanOrEqualTo(Duration.ofMinutes(20));
        assertThat(until).isLessThan(Duration.ofHours(2));

        // Second correct should push to ~1 day
        before = Instant.now();
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, null);
        di = drillItemDao.findByChallengeId(ch.getId()).get(0);
        assertThat(di.getTimesSeen()).isEqualTo(2);
        assertThat(di.getStreak()).isEqualTo(2);
        until = Duration.between(before, di.getNextDueAt());
        assertThat(until).isGreaterThanOrEqualTo(Duration.ofHours(12));
        assertThat(until).isLessThan(Duration.ofDays(2));
    }

    @Test
    void recordOutcome_incorrect_resets_streak_and_short_delay() {
        Challenge ch = newChallenge("Valid Parentheses");
        Instant before = Instant.now();
        drillService.recordOutcome(ch.getId(), Outcome.INCORRECT, null);
        DrillItem di = drillItemDao.findByChallengeId(ch.getId()).get(0);
        assertThat(di.getTimesSeen()).isEqualTo(1);
        assertThat(di.getStreak()).isZero();
        Duration until = Duration.between(before, di.getNextDueAt());
        assertThat(until).isGreaterThanOrEqualTo(Duration.ofMinutes(4));
        assertThat(until).isLessThan(Duration.ofMinutes(15));
    }

    @Test
    void recordOutcome_skipped_keeps_streak_and_ten_min_delay() {
        Challenge ch = newChallenge("Merge Intervals");
        // Build up a streak first
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, null);
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, null);
        int streakBefore = drillItemDao.findByChallengeId(ch.getId()).get(0).getStreak();
        Instant before = Instant.now();
        drillService.recordOutcome(ch.getId(), Outcome.SKIPPED, null);
        DrillItem di = drillItemDao.findByChallengeId(ch.getId()).get(0);
        assertThat(di.getTimesSeen()).isEqualTo(3);
        assertThat(di.getStreak()).isEqualTo(streakBefore);
        Duration until = Duration.between(before, di.getNextDueAt());
        assertThat(until).isGreaterThanOrEqualTo(Duration.ofMinutes(8));
        assertThat(until).isLessThan(Duration.ofMinutes(20));
    }

    @Test
    void getDueQueue_returns_due_sorted_and_excludes_future() {
        Challenge a = newChallenge("A");
        Challenge b = newChallenge("B");
        Challenge c = newChallenge("C");

        DrillItem diA = drillItemDao.save(new DrillItem(a)); // nextDueAt null
        DrillItem diB = new DrillItem(b);
        diB.setNextDueAt(Instant.now().minus(Duration.ofMinutes(1))); // due
        drillItemDao.save(diB);
        DrillItem diC = new DrillItem(c);
        diC.setNextDueAt(Instant.now().plus(Duration.ofMinutes(2))); // future
        drillItemDao.save(diC);

        List<DrillItem> queue = drillService.getDueQueue(10);
        assertThat(queue).hasSize(2);
        assertThat(queue.get(0).getId()).isEqualTo(diA.getId()); // null first
        assertThat(queue.get(1).getId()).isEqualTo(diB.getId()); // then past

        // Limit respected
        List<DrillItem> one = drillService.getDueQueue(1);
        assertThat(one).hasSize(1);
        assertThat(one.get(0).getId()).isEqualTo(diA.getId());
    }

    @Test
    void getDueQueue_when_none_due_returns_soonest_future_singleton() {
        Challenge d = newChallenge("D");
        Challenge e = newChallenge("E");
        DrillItem diD = new DrillItem(d);
        diD.setNextDueAt(Instant.now().plus(Duration.ofMinutes(1)));
        drillItemDao.save(diD);
        DrillItem diE = new DrillItem(e);
        diE.setNextDueAt(Instant.now().plus(Duration.ofMinutes(3)));
        drillItemDao.save(diE);

        List<DrillItem> queue = drillService.getDueQueue(5);
        assertThat(queue).hasSize(1);
        assertThat(queue.get(0).getId()).isEqualTo(diD.getId());
    }

    @Test
    void version_increments_on_update() {
        Challenge ch = newChallenge("Version Check");

        // First write should create the DrillItem and set initial version
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, null);
        DrillItem first = drillItemDao.findByChallengeId(ch.getId()).get(0);
        Long v1 = first.getVersion();
        assertThat(v1).as("version after first save").isNotNull();

        // Second write should increment version
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, null);
        DrillItem second = drillItemDao.findByChallengeId(ch.getId()).get(0);
        Long v2 = second.getVersion();
        assertThat(v2).as("version after second save").isNotNull();
        assertThat(v2).isGreaterThan(v1);
    }
}
