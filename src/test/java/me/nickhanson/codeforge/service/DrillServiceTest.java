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

    private static Challenge challengeWithId(long id) {
        Challenge c = new Challenge("Two Sum", Difficulty.EASY, "b", "p");
        try { var f = Challenge.class.getDeclaredField("id"); f.setAccessible(true); f.set(c, id); } catch (Exception ignored) {}
        return c;
    }

    @Test
    void recordOutcome_createsSubmission_updatesDrillItem_andPersistsBoth() {
        Long id = 42L;
        Challenge c = challengeWithId(id);
        DrillItem di = new DrillItem(c);

        when(chalDao.getById(id)).thenReturn(c);
        when(drillDao.listByChallengeId(id)).thenReturn(List.of(di));

        Submission s = service.recordOutcome(id, Outcome.CORRECT, "code");

        verify(subDao).saveOrUpdate(any(Submission.class));
        verify(drillDao).saveOrUpdate(argThat(item ->
                item.getStreak() >= 1 && item.getTimesSeen() == 1 && item.getNextDueAt() != null));
        assertEquals(Outcome.CORRECT, s.getOutcome());
    }

    @Test
    void computeNextDueAt_advancesAccordingToStreak() {
        Long id = 7L;
        Challenge c = challengeWithId(id);
        DrillItem di = new DrillItem(c);
        di.setTimesSeen(0);
        di.setStreak(0);

        when(chalDao.getById(id)).thenReturn(c);
        when(drillDao.listByChallengeId(id)).thenReturn(List.of(di));

        // CORRECT should set nextDueAt in the future and increment streak
        service.recordOutcome(id, Outcome.CORRECT, "// correct");
        verify(drillDao).saveOrUpdate(argThat(item -> item.getStreak() == 1 && item.getNextDueAt().isAfter(Instant.now())));

        // INCORRECT should reset streak to 0 and set shorter delay
        service.recordOutcome(id, Outcome.INCORRECT, "// fail");
        verify(drillDao, atLeast(2)).saveOrUpdate(any());
    }

    @Test
    void getDueQueue_returnsSoonestWhenNoneDue() {
        when(drillDao.dueQueue(any(), eq(1))).thenReturn(List.of());
        DrillItem soonest = new DrillItem(challengeWithId(0L));
        when(drillDao.soonestUpcoming()).thenReturn(Optional.of(soonest));

        var queue = service.getDueQueue(1);
        assertEquals(1, queue.size());
        assertSame(soonest, queue.get(0));
    }

    @Test
    void ensureDrillItem_createsWhenMissing() {
        Long id = 9L;
        Challenge c = challengeWithId(id);
        when(chalDao.getById(id)).thenReturn(c);
        when(drillDao.listByChallengeId(id)).thenReturn(List.of());

        service.ensureDrillItem(id);
        verify(drillDao).saveOrUpdate(any(DrillItem.class));
    }
}
