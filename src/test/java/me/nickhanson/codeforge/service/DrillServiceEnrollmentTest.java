package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.DrillItem;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.persistence.DrillItemDao;
import me.nickhanson.codeforge.persistence.SubmissionDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrillServiceEnrollmentTest {

    @Mock DrillItemDao drillItemDao;
    @Mock SubmissionDao submissionDao;
    @Mock ChallengeDao challengeDao;

    @InjectMocks DrillService service;

    @Test
    void ensureEnrollmentForUser_createsOnlyMissing() throws Exception {
        String userId = "enroll-user";
        Challenge a = new Challenge("A", Difficulty.EASY, "", "");
        Challenge b = new Challenge("B", Difficulty.EASY, "", "");
        // assign IDs via reflection
        var f = Challenge.class.getDeclaredField("id"); f.setAccessible(true);
        f.set(a, 1L); f.set(b, 2L);

        DrillItem existing = new DrillItem(a);
        existing.setUserId(userId);

        when(drillItemDao.listByChallengeIdAndUser(1L, userId)).thenReturn(List.of(existing));
        when(drillItemDao.listByChallengeIdAndUser(2L, userId)).thenReturn(List.of());

        int created = service.ensureEnrollmentForUser(List.of(a, b), userId);

        assertEquals(1, created, "Should create exactly one missing DrillItem");
        verify(drillItemDao, times(1)).saveOrUpdate(any(DrillItem.class));
    }
}
