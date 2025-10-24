package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DAO tests for ChallengeDao using JPA slice.
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(ChallengeDao.class)
class ChallengeDaoTest {

    @Autowired
    private ChallengeDao dao;

    /**
     * Creates a sample Challenge entity for testing purposes.
     *
     * @return a Challenge object with predefined values.
     */
    private Challenge sample() {
        return new Challenge(
                "Two Sum",
                Difficulty.EASY,
                "Find indices of two numbers that add up to target.",
                "## Prompt\nGiven an array nums and an integer target..."
        );
    }

    /**
     * Tests that a Challenge entity can be saved and retrieved by its ID.
     * Verifies that the saved entity has a non-null ID and that the retrieved
     * entity matches the saved entity's properties.
     */
    @Test
    void save_and_findById_success() {
        Challenge saved = dao.save(sample());
        assertThat(saved.getId()).isNotNull();

        Optional<Challenge> found = dao.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Two Sum");
        assertThat(found.get().getDifficulty()).isEqualTo(Difficulty.EASY);
    }

    /**
     * Tests that the findAll() method returns a list of all saved Challenge entities.
     */
    @Test
    void findAll_returns_list() {
        dao.save(sample());
        dao.save(new Challenge("Reverse String", Difficulty.EASY, "Reverse characters", "details..."));
        List<Challenge> list = dao.findAll();
        assertThat(list).hasSize(2);
    }

    @Test
    void findByDifficulty_filters_results() {
        dao.save(new Challenge("Reverse String", Difficulty.EASY, "Reverse characters", "details..."));
        dao.save(new Challenge("Binary Search", Difficulty.MEDIUM, "Search sorted array", "details..."));
        List<Challenge> easy = dao.findByDifficulty(Difficulty.EASY);
        assertThat(easy).extracting(Challenge::getDifficulty).containsOnly(Difficulty.EASY);
    }

    /**
     * Tests that the findByTitle() method returns an empty result when no Challenge
     * entity matches the given title.
     */
    @Test
    void findByTitle_missing_returns_empty() {
        assertThat(dao.findByTitle("Nope")).isEmpty();
    }

    /**
     * Tests that the deleteById() method removes a Challenge entity from the repository.
     * Verifies that the entity is no longer present after deletion.
     */
    @Test
    void deleteById_removes_entity() {
        Challenge saved = dao.save(sample());
        Long id = saved.getId();
        dao.deleteById(id);
        assertThat(dao.findById(id)).isEmpty();
    }
}