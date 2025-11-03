package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrillItemDaoTest extends DaoTestBase {

    private final ChallengeDao challengeDao = new ChallengeDao();
    private final DrillItemDao drillDao = new DrillItemDao();

    private Challenge seedChallenge(String title) {
        Challenge ch = new Challenge(title, Difficulty.EASY, "", "...");
        challengeDao.saveOrUpdate(ch);
        return ch;
    }

    @Test
    void create_assignsId_and_relatesToChallenge() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch);
        drillDao.saveOrUpdate(di);
        assertNotNull(di.getId());
        assertEquals(ch.getId(), di.getChallenge().getId());
    }

    @Test
    void listByChallenge_returnsItems() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch);
        drillDao.saveOrUpdate(di);
        List<DrillItem> items = drillDao.listByChallengeId(ch.getId());
        assertEquals(1, items.size());
        assertEquals(ch.getId(), items.get(0).getChallenge().getId());
    }

    @Test
    void dueQueue_ordersNullFirst_thenByTime() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem a = new DrillItem(ch); a.setNextDueAt(null); drillDao.saveOrUpdate(a);
        DrillItem b = new DrillItem(ch); b.setNextDueAt(Instant.now().minusSeconds(60)); drillDao.saveOrUpdate(b);
        DrillItem c = new DrillItem(ch); c.setNextDueAt(Instant.now().plusSeconds(60)); drillDao.saveOrUpdate(c);

        List<DrillItem> queue = drillDao.dueQueue(Instant.now(), 10);
        assertEquals(2, queue.size()); // null + past-due; future item excluded
        assertNull(queue.get(0).getNextDueAt());
        assertTrue(queue.get(1).getNextDueAt().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void delete_item_keepsChallenge() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch);
        drillDao.saveOrUpdate(di);
        drillDao.delete(di);
        assertEquals(1, challengeDao.getAll().size());
        assertEquals(0, drillDao.getAll().size());
    }

    @Test
    void delete_challenge_requiresCleaningDependents() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch); drillDao.saveOrUpdate(di);
        // clean dependents then delete parent
        drillDao.delete(di);
        challengeDao.delete(ch);
        assertEquals(0, challengeDao.getAll().size());
    }
}
