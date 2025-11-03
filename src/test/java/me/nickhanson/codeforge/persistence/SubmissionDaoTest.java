package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionDaoTest extends DaoTestBase {

    private final ChallengeDao challengeDao = new ChallengeDao();
    private final DrillItemDao drillDao = new DrillItemDao();
    private final SubmissionDao submissionDao = new SubmissionDao();

    private Challenge seedChallenge(String title) {
        Challenge ch = new Challenge(title, Difficulty.EASY, "", "...");
        challengeDao.saveOrUpdate(ch);
        return ch;
    }

    @Test
    void create_assignsId() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s = new Submission(ch, Outcome.INCORRECT, "int i=0;");
        submissionDao.saveOrUpdate(s);
        assertNotNull(s.getId());
    }

    @Test
    void read_getById_returnsSaved() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s = new Submission(ch, Outcome.CORRECT, "// correct");
        submissionDao.saveOrUpdate(s);
        Submission found = submissionDao.getById(s.getId());
        assertNotNull(found);
        assertEquals(Outcome.CORRECT, found.getOutcome());
        assertEquals(ch.getId(), found.getChallenge().getId());
    }

    @Test
    void listByChallenge_returnsOnlyMatching() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s1 = new Submission(ch, Outcome.CORRECT, null);
        submissionDao.saveOrUpdate(s1);
        Challenge other = seedChallenge("Two Sum");
        Submission s2 = new Submission(other, Outcome.INCORRECT, null);
        submissionDao.saveOrUpdate(s2);
        List<Submission> list = submissionDao.listByChallengeId(ch.getId());
        assertEquals(1, list.size());
        assertEquals(ch.getId(), list.get(0).getChallenge().getId());
    }

    @Test
    void delete_submission_keepsChallenge() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s = new Submission(ch, Outcome.SKIPPED, null);
        submissionDao.saveOrUpdate(s);
        submissionDao.delete(s);
        assertEquals(1, challengeDao.getAll().size());
        assertEquals(0, submissionDao.getAll().size());
    }

    @Test
    void delete_challenge_requiresCleaningDependents() {
        Challenge ch = seedChallenge("Reverse String");
        // Keep FK safe: seed a DrillItem and a Submission
        DrillItem di = new DrillItem(ch); drillDao.saveOrUpdate(di);
        Submission s = new Submission(ch, Outcome.CORRECT, null); submissionDao.saveOrUpdate(s);

        // Clean dependents first, then delete parent
        drillDao.delete(di);
        submissionDao.delete(s);
        challengeDao.delete(ch);

        assertEquals(0, challengeDao.getAll().size());
        assertEquals(0, submissionDao.getAll().size());
    }
}
