package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.DrillItem;
import me.nickhanson.codeforge.entity.Submission;
import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeDaoTest extends DaoTestBase {

    private final ChallengeDao dao = new ChallengeDao();
    private final DrillItemDao drillDao = new DrillItemDao();
    private final SubmissionDao submissionDao = new SubmissionDao();

    @Test
    void create_assignsId() {
        Challenge c1 = new Challenge("Two Sum", Difficulty.EASY, "Find two numbers sum to target", "...");
        dao.saveOrUpdate(c1);
        assertNotNull(c1.getId());
    }

    @Test
    void read_getById_returnsSavedEntity() {
        Challenge c1 = new Challenge("Two Sum", Difficulty.EASY, "Find two numbers sum to target", "...");
        dao.saveOrUpdate(c1);
        Challenge found = dao.getById(c1.getId());
        assertNotNull(found);
        assertEquals("Two Sum", found.getTitle());
        assertEquals(Difficulty.EASY, found.getDifficulty());
    }

    @Test
    void update_persistsChanges() {
        Challenge c1 = new Challenge("Two Sum", Difficulty.EASY, "Find two numbers sum to target", "...");
        dao.saveOrUpdate(c1);
        c1.setTitle("Two Sum v2");
        dao.saveOrUpdate(c1);
        Challenge updated = dao.getById(c1.getId());
        assertEquals("Two Sum v2", updated.getTitle());
    }

    @Test
    void getAll_returnsAll() {
        dao.saveOrUpdate(new Challenge("Two Sum", Difficulty.EASY, "", "..."));
        dao.saveOrUpdate(new Challenge("Three Sum", Difficulty.MEDIUM, "", "..."));
        List<Challenge> all = dao.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void existsTitleIgnoreCase_trueAfterInsert() {
        dao.saveOrUpdate(new Challenge("Two Sum", Difficulty.EASY, "", "..."));
        assertTrue(dao.existsTitleIgnoreCase("two sum"));
    }

    @Test
    void existsTitleForOtherIgnoreCase_falseForSameEntity() {
        Challenge c1 = new Challenge("Two Sum", Difficulty.EASY, "", "...");
        dao.saveOrUpdate(c1);
        assertFalse(dao.existsTitleForOtherIgnoreCase("two sum", c1.getId()));
    }

    @Test
    void findByDifficulty_returnsOnlyMatching() {
        dao.saveOrUpdate(new Challenge("Two Sum", Difficulty.EASY, "", "..."));
        dao.saveOrUpdate(new Challenge("Three Sum", Difficulty.MEDIUM, "", "..."));
        assertEquals(1, dao.findByDifficulty(Difficulty.EASY).size());
    }

    @Test
    void delete_removesRow() {
        Challenge c1 = new Challenge("Two Sum", Difficulty.EASY, "", "...");
        dao.saveOrUpdate(c1);
        Long id = c1.getId();
        dao.delete(c1);
        assertNull(dao.getById(id));
        assertEquals(0, dao.getAll().size());
    }

    @Test
    void delete_withDependents_failsWithoutCleaning() {
        Challenge ch = new Challenge("Dependent Test", Difficulty.EASY, "", "...");
        dao.saveOrUpdate(ch);

        DrillItem di = new DrillItem(ch);
        drillDao.saveOrUpdate(di);
        Submission s = new Submission(ch, Outcome.CORRECT, null);
        submissionDao.saveOrUpdate(s);

        // When / Then – should throw some persistence-level exception
        assertThrows(Exception.class, () -> dao.delete(ch));

        // Verify parent and children still exist
        assertNotNull(dao.getById(ch.getId()));
        assertEquals(1, drillDao.listByChallengeId(ch.getId()).size());
        assertEquals(1, submissionDao.listByChallengeId(ch.getId()).size());

        // Clean up for test isolation
        drillDao.delete(di);
        submissionDao.delete(s);
        dao.delete(ch);
    }
}
