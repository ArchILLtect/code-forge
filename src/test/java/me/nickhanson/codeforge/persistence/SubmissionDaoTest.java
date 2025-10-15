package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({SubmissionDao.class, ChallengeDao.class})
class SubmissionDaoTest {

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private ChallengeDao challengeDao;

    private Challenge sampleChallenge() {
        return new Challenge(
                "Two Sum",
                Difficulty.EASY,
                "Find indices of two numbers that add up to target.",
                "## Prompt..."
        );
    }

    @Test
    void save_and_findById_success() {
        Challenge ch = challengeDao.save(sampleChallenge());
        Submission s = new Submission(ch, Outcome.CORRECT, "code...");
        Submission saved = submissionDao.save(s);
        assertThat(saved.getId()).isNotNull();

        Optional<Submission> found = submissionDao.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getOutcome()).isEqualTo(Outcome.CORRECT);
        assertThat(found.get().getChallenge().getId()).isEqualTo(ch.getId());
    }

    @Test
    void findByChallengeId_returns_list() {
        Challenge ch = challengeDao.save(sampleChallenge());
        submissionDao.save(new Submission(ch, Outcome.CORRECT, null));
        submissionDao.save(new Submission(ch, Outcome.INCORRECT, null));

        List<Submission> list = submissionDao.findByChallengeId(ch.getId());
        assertThat(list).hasSize(2);
    }

    @Test
    void update_outcome_success() {
        Challenge ch = challengeDao.save(sampleChallenge());
        Submission s = submissionDao.save(new Submission(ch, Outcome.INCORRECT, null));
        s.setOutcome(Outcome.ACCEPTABLE);
        Submission updated = submissionDao.save(s);
        assertThat(updated.getOutcome()).isEqualTo(Outcome.ACCEPTABLE);
    }

    @Test
    void deleteById_removes_entity() {
        Challenge ch = challengeDao.save(sampleChallenge());
        Submission s = submissionDao.save(new Submission(ch, Outcome.SKIPPED, null));
        Long id = s.getId();
        submissionDao.deleteById(id);
        assertThat(submissionDao.findById(id)).isEmpty();
    }
}
