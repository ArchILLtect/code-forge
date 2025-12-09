package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.DrillItem;
import me.nickhanson.codeforge.entity.Submission;
import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;
import me.nickhanson.codeforge.testutil.DbReset;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeDaoTest extends DbReset {

    private final ChallengeDao dao = new ChallengeDao();
    private final DrillItemDao drillDao = new DrillItemDao();
    private final SubmissionDao submissionDao = new SubmissionDao();

    @Test
    void create_assignsId() {
        Challenge c1 = new Challenge("Unique Alpha", Difficulty.EASY, "Find two numbers sum to target", "...");
        dao.saveOrUpdate(c1);
        assertNotNull(c1.getId());
    }

    @Test
    void read_getById_returnsSavedEntity() {
        Challenge c1 = new Challenge("Unique Beta", Difficulty.EASY, "Find two numbers sum to target", "...");
        dao.saveOrUpdate(c1);
        Challenge found = dao.getById(c1.getId());
        assertNotNull(found);
        assertEquals("Unique Beta", found.getTitle());
        assertEquals(Difficulty.EASY, found.getDifficulty());
    }

    @Test
    void update_persistsChanges() {
        Challenge c1 = new Challenge("Unique Gamma", Difficulty.EASY, "Find two numbers sum to target", "...");
        dao.saveOrUpdate(c1);
        c1.setTitle("Unique Gamma v2");
        dao.saveOrUpdate(c1);
        Challenge updated = dao.getById(c1.getId());
        assertEquals("Unique Gamma v2", updated.getTitle());
    }

    @Test
    void getAll_returnsAll() {
        int before = dao.getAll().size();
        dao.saveOrUpdate(new Challenge("Unique Delta", Difficulty.EASY, "", "..."));
        dao.saveOrUpdate(new Challenge("Unique Epsilon", Difficulty.MEDIUM, "", "..."));
        List<Challenge> all = dao.getAll();
        assertEquals(before + 2, all.size());
    }

    @Test
    void existsTitleIgnoreCase_trueAfterInsert() {
        dao.saveOrUpdate(new Challenge("Unique Zeta", Difficulty.EASY, "", "..."));
        assertTrue(dao.existsTitleIgnoreCase("unique zeta"));
    }

    @Test
    void existsTitleForOtherIgnoreCase_falseForSameEntity() {
        Challenge c1 = new Challenge("Unique Eta", Difficulty.EASY, "", "...");
        dao.saveOrUpdate(c1);
        assertFalse(dao.existsTitleForOtherIgnoreCase("unique eta", c1.getId()));
    }

    @Test
    void findByDifficulty_returnsOnlyMatching() {
        int beforeEasy = dao.findByDifficulty(Difficulty.EASY).size();
        int beforeMedium = dao.findByDifficulty(Difficulty.MEDIUM).size();
        dao.saveOrUpdate(new Challenge("Unique Theta", Difficulty.EASY, "", "..."));
        dao.saveOrUpdate(new Challenge("Unique Iota", Difficulty.MEDIUM, "", "..."));
        assertEquals(beforeEasy + 1, dao.findByDifficulty(Difficulty.EASY).size());
        assertEquals(beforeMedium + 1, dao.findByDifficulty(Difficulty.MEDIUM).size());
    }

    @Test
    void delete_removesRow() {
        int before = dao.getAll().size();
        Challenge c1 = new Challenge("Unique Kappa", Difficulty.EASY, "", "...");
        dao.saveOrUpdate(c1);
        int afterInsert = dao.getAll().size();
        assertEquals(before + 1, afterInsert);
        dao.delete(c1);
        int afterDelete = dao.getAll().size();
        assertEquals(before, afterDelete);
    }

    @Test
    void delete_withDependents_cascadesToChildren() {
        Challenge ch = new Challenge("Dependent Test", Difficulty.EASY, "", "...");
        dao.saveOrUpdate(ch);

        DrillItem di = new DrillItem(ch);
        di.setUserId("unit-test-user");
        drillDao.saveOrUpdate(di);

        Submission s = new Submission(ch, Outcome.CORRECT, null);
        s.setUserId("unit-test-user");
        submissionDao.saveOrUpdate(s);

        // When – delete the parent
        dao.delete(ch);

        // Then – parent is gone
        assertNull(dao.getById(ch.getId()));

        // And children are gone because of ON DELETE CASCADE
        assertEquals(0, drillDao.listByChallengeId(ch.getId()).size());
        assertEquals(0, submissionDao.listByChallengeId(ch.getId()).size());
    }
}
