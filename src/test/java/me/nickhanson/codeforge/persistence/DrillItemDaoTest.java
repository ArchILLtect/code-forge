package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.*;
import org.junit.jupiter.api.Test;
import me.nickhanson.codeforge.testutil.DbReset;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrillItemDaoTest extends DbReset {

    private final ChallengeDao challengeDao = new ChallengeDao();
    private final DrillItemDao drillDao = new DrillItemDao();

    private static final String USER = "unit-test-user";

    private Challenge seedChallenge(String title) {
        Challenge ch = new Challenge(title, Difficulty.EASY, "", "...");
        challengeDao.saveOrUpdate(ch);
        return ch;
    }

    @Test
    void create_assignsId_and_relatesToChallenge() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch);
        di.setUserId(USER);
        drillDao.saveOrUpdate(di);
        assertNotNull(di.getId());
        assertEquals(ch.getId(), di.getChallenge().getId());
    }

    @Test
    void listByChallenge_returnsItems() {
        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch);
        di.setUserId(USER);
        drillDao.saveOrUpdate(di);
        List<DrillItem> items = drillDao.listByChallengeId(ch.getId());
        assertEquals(1, items.size());
        assertEquals(ch.getId(), items.get(0).getChallenge().getId());
    }

    @Test
    void dueQueue_ordersNullFirst_thenByTime() {
        // Ensure we control the data set
        drillDao.getAll().forEach(drillDao::delete);

        Challenge chNull   = seedChallenge("FizzBuzz Null");
        Challenge chPast   = seedChallenge("FizzBuzz Past");
        Challenge chFuture = seedChallenge("FizzBuzz Future");

        DrillItem a = new DrillItem(chNull);
        a.setUserId(USER);
        a.setNextDueAt(null);
        drillDao.saveOrUpdate(a);

        DrillItem b = new DrillItem(chPast);
        b.setUserId(USER);
        b.setNextDueAt(Instant.now().minusSeconds(60));
        drillDao.saveOrUpdate(b);

        DrillItem c = new DrillItem(chFuture);
        c.setUserId(USER);
        c.setNextDueAt(Instant.now().plusSeconds(60));
        drillDao.saveOrUpdate(c);

        List<DrillItem> queue = drillDao.dueQueue(Instant.now(), 10);

        // We expect ONLY the null + past item to be "due"
        assertEquals(2, queue.size(), "Due queue should include null and past, but not future");

        // 1) null first
        assertNull(queue.get(0).getNextDueAt());

        // 2) then the past-due item
        assertNotNull(queue.get(1).getNextDueAt());
        assertTrue(queue.get(1).getNextDueAt().isBefore(Instant.now()));

        // 3) and the future item is NOT in the queue
        List<Long> challengeIds = queue.stream()
                .map(di -> di.getChallenge().getId())
                .toList();
        assertFalse(challengeIds.contains(chFuture.getId()));
    }

    @Test
    void delete_item_keepsChallenge() {
        int beforeChallengeCount = challengeDao.getAll().size();
        int beforeItemCount = drillDao.getAll().size();

        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch);
        di.setUserId(USER);
        drillDao.saveOrUpdate(di);

        drillDao.delete(di);

        assertEquals(beforeChallengeCount + 1, challengeDao.getAll().size());
        assertEquals(beforeItemCount, drillDao.getAll().size());
    }

    @Test
    void delete_challenge_requiresCleaningDependents() {
        int beforeChallengeCount = challengeDao.getAll().size();

        Challenge ch = seedChallenge("FizzBuzz");
        DrillItem di = new DrillItem(ch); di.setUserId(USER); drillDao.saveOrUpdate(di);
        // clean dependents then delete parent
        drillDao.delete(di);
        challengeDao.delete(ch);
        assertEquals(beforeChallengeCount, challengeDao.getAll().size());
    }
}
