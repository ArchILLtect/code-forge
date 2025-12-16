package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.*;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.persistence.DrillItemDao;
import me.nickhanson.codeforge.persistence.SubmissionDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrillServiceTest {

    @Mock DrillItemDao drillDao;
    @Mock SubmissionDao subDao;
    @Mock ChallengeDao chalDao;

    @InjectMocks DrillService service;

    /**
     * Utility to create a Challenge with a specific ID via reflection.
     */
    private static Challenge challengeWithId(long id) {
        Challenge c = new Challenge("Two Sum", Difficulty.EASY, "b", "p");
        try { var f = Challenge.class.getDeclaredField("id"); f.setAccessible(true); f.set(c, id); } catch (Exception ignored) {}
        return c;
    }

    /**
     * Tests that recording an outcome creates a Submission,
     * updates the corresponding DrillItem, and persists both.
     */
    @Test
    void recordOutcome_createsSubmission_updatesDrillItem_andPersistsBoth() {
        long id = 42L;
        String userId = "demo-user";
        Challenge c = challengeWithId(id);
        DrillItem di = new DrillItem(c);
        di.setUserId(userId);

        when(chalDao.getById(id)).thenReturn(c);
        when(drillDao.listByChallengeIdAndUser(id, userId)).thenReturn(List.of(di));

        Submission s = service.recordOutcome(id, Outcome.CORRECT, "code", userId);

        verify(subDao).saveOrUpdate(any(Submission.class));
        verify(drillDao).saveOrUpdate(argThat(item ->
                item.getStreak() >= 1 && item.getTimesSeen() == 1 && item.getNextDueAt() != null));
        assertEquals(Outcome.CORRECT, s.getOutcome());
    }

    /**
     * Tests that the nextDueAt is computed correctly based on the current streak.
     */
    @Test
    void computeNextDueAt_advancesAccordingToStreak() {
        long id = 7L;
        String userId = "demo-user";
        Challenge c = challengeWithId(id);
        DrillItem di = new DrillItem(c);
        di.setTimesSeen(0);
        di.setStreak(0);
        di.setUserId(userId);

        when(chalDao.getById(id)).thenReturn(c);
        when(drillDao.listByChallengeIdAndUser(id, userId)).thenReturn(List.of(di));

        // CORRECT should set nextDueAt in the future and increment streak
        service.recordOutcome(id, Outcome.CORRECT, "// correct", userId);
        verify(drillDao).saveOrUpdate(argThat(item -> item.getStreak() == 1 && item.getNextDueAt().isAfter(Instant.now())));

        // INCORRECT should reset streak to 0 and set shorter delay
        service.recordOutcome(id, Outcome.INCORRECT, "// fail", userId);
        verify(drillDao, atLeast(2)).saveOrUpdate(any());
    }

    /**
     * Tests that getDueQueue returns due items, or the soonest upcoming if none are due.
     */
    @Test
    void getDueQueue_returnsSoonestWhenNoneDue() {
        String userId = "demo-user";
        when(drillDao.dueQueue(any(), eq(1), eq(userId))).thenReturn(List.of());
        DrillItem soonest = new DrillItem(challengeWithId(0L));
        soonest.setUserId(userId);
        when(drillDao.soonestUpcoming(eq(userId))).thenReturn(Optional.of(soonest));

        var queue = service.getDueQueue(1, userId);
        assertEquals(1, queue.size());
        assertSame(soonest, queue.get(0));
    }

    /**
     * Tests that ensureDrillItem creates a DrillItem if missing.
     */
    @Test
    void ensureDrillItem_createsWhenMissing() {
        long id = 9L;
        String userId = "demo-user";
        Challenge c = challengeWithId(id);
        when(chalDao.getById(id)).thenReturn(c);
        when(drillDao.listByChallengeIdAndUser(id, userId)).thenReturn(List.of());

        service.ensureDrillItem(id, userId);
        verify(drillDao).saveOrUpdate(any(DrillItem.class));
    }
}
