package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.*;
import org.junit.jupiter.api.Test;
import me.nickhanson.codeforge.testutil.DbReset;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionDaoTest extends DbReset {

    private final ChallengeDao challengeDao = new ChallengeDao();
    private final DrillItemDao drillDao = new DrillItemDao();
    private final SubmissionDao submissionDao = new SubmissionDao();

    private static final String USER = "unit-test-user";

    /**
     * Helper method to create and persist a Challenge with the given title.
     */
    private Challenge seedChallenge(String title) {
        Challenge ch = new Challenge(title, Difficulty.EASY, "", "...");
        challengeDao.saveOrUpdate(ch);
        return ch;
    }

    /**
     * Verifies that creating a Submission assigns it an ID.
     */
    @Test
    void create_assignsId() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s = new Submission(ch, Outcome.INCORRECT, "int i=0;");
        s.setUserId(USER);
        submissionDao.saveOrUpdate(s);
        assertNotNull(s.getId());
    }

    /**
     * Verifies that retrieving a Submission by ID returns the saved entity.
     */
    @Test
    void read_getById_returnsSaved() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s = new Submission(ch, Outcome.CORRECT, "// correct");
        s.setUserId(USER);
        submissionDao.saveOrUpdate(s);
        Submission found = submissionDao.getById(s.getId());
        assertNotNull(found);
        assertEquals(Outcome.CORRECT, found.getOutcome());
        assertEquals(ch.getId(), found.getChallenge().getId());
    }

    /**
     * Verifies that listing Submissions by Challenge ID returns only matching Submissions.
     */
    @Test
    void listByChallenge_returnsOnlyMatching() {
        Challenge ch = seedChallenge("Reverse String");
        Submission s1 = new Submission(ch, Outcome.CORRECT, null);
        s1.setUserId(USER);
        submissionDao.saveOrUpdate(s1);
        Challenge other = seedChallenge("Unique Lambda");
        Submission s2 = new Submission(other, Outcome.INCORRECT, null);
        s2.setUserId(USER);
        submissionDao.saveOrUpdate(s2);
        List<Submission> list = submissionDao.listByChallengeId(ch.getId());
        assertEquals(1, list.size());
        assertEquals(ch.getId(), list.get(0).getChallenge().getId());
    }

    /**
     * Verifies that deleting a Submission does not delete its associated Challenge.
     */
    @Test
    void delete_submission_keepsChallenge() {
        int beforeChallengeCount = challengeDao.getAll().size();
        int beforeSubmissionCount = submissionDao.getAll().size();

        Challenge ch = seedChallenge("Reverse String");
        Submission s = new Submission(ch, Outcome.SKIPPED, null);
        s.setUserId(USER);
        submissionDao.saveOrUpdate(s);
        submissionDao.delete(s);

        assertEquals(beforeChallengeCount + 1, challengeDao.getAll().size());
        assertEquals(beforeSubmissionCount, submissionDao.getAll().size());
    }

    /**
     * Verifies that deleting a Challenge requires cleaning up dependent DrillItems and Submissions first.
     */
    @Test
    void delete_challenge_requiresCleaningDependents() {
        int beforeChallengeCount = challengeDao.getAll().size();
        int beforeSubmissionCount = submissionDao.getAll().size();

        Challenge ch = seedChallenge("Reverse String");
        // Keep FK safe: seed a DrillItem and a Submission
        DrillItem di = new DrillItem(ch); di.setUserId(USER); drillDao.saveOrUpdate(di);
        Submission s = new Submission(ch, Outcome.CORRECT, null); s.setUserId(USER); submissionDao.saveOrUpdate(s);

        // Clean dependents first, then delete parent
        drillDao.delete(di);
        submissionDao.delete(s);
        challengeDao.delete(ch);

        assertEquals(beforeChallengeCount, challengeDao.getAll().size());
    }
}
