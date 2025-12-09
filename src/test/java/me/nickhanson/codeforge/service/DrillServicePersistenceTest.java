package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.Outcome;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.persistence.DrillItemDao;
import me.nickhanson.codeforge.entity.DrillItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testsupport.DbReset;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class DrillServicePersistenceTest extends DbReset {

    private DrillService drillService;
    private ChallengeDao challengeDao;
    private DrillItemDao drillItemDao;

    @BeforeEach
    void setup() {
        drillService = new DrillService();
        challengeDao = new ChallengeDao();
        drillItemDao = new DrillItemDao();
    }

    @Test
    void recordOutcome_updatesDrillItemFields() {
        // Seed a challenge
        Challenge ch = new Challenge("Temp Test", Difficulty.EASY, "", "");
        challengeDao.saveOrUpdate(ch);
        assertNotNull(ch.getId());

        String userId = "persistence-test-user";
        DrillItem di = drillService.ensureDrillItem(ch.getId(), userId);
        Instant before = di.getNextDueAt();
        int seenBefore = di.getTimesSeen();

        // Record a CORRECT outcome
        drillService.recordOutcome(ch.getId(), Outcome.CORRECT, "code", userId);

        // Fetch updated item via listByChallengeIdAndUser
        java.util.List<DrillItem> items = drillItemDao.listByChallengeIdAndUser(ch.getId(), userId);
        assertFalse(items.isEmpty());
        DrillItem refreshed = items.get(0);
        assertEquals(seenBefore + 1, refreshed.getTimesSeen());
        assertNotNull(refreshed.getNextDueAt());
        if (before != null) {
            assertTrue(refreshed.getNextDueAt().isAfter(before));
        }
    }
}
