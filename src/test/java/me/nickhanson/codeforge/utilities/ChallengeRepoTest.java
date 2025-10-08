package me.nickhanson.codeforge.utilities;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the ChallengeRepo repository.
 * This class uses Spring Boot's @DataJpaTest to test JPA repository methods.
 */
@DataJpaTest
@ActiveProfiles("test")
class ChallengeRepoTest {

    @Autowired
    private ChallengeRepo repo;

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
        var saved = repo.save(sample());
        assertThat(saved.getId()).isNotNull();

        var found = repo.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Two Sum");
        assertThat(found.get().getDifficulty()).isEqualTo(Difficulty.EASY);
    }

    /**
     * Tests that the findAll() method returns a list of all saved Challenge entities.
     * Verifies that the size of the returned list matches the number of saved entities.
     */
    @Test
    void findAll_returns_list() {
        repo.save(sample());
        repo.save(new Challenge("Reverse String", Difficulty.EASY, "Reverse characters", "details..."));
        List<Challenge> all = repo.findAll();
        assertThat(all).hasSize(2);
    }

    /**
     * Tests that the findByTitle() method returns an empty result when no Challenge
     * entity matches the given title.
     */
    @Test
    void findByTitle_missing_returns_empty() {
        assertThat(repo.findByTitle("Nope")).isEmpty();
    }

    /**
     * Tests that the deleteById() method removes a Challenge entity from the repository.
     * Verifies that the entity is no longer present after deletion.
     */
    @Test
    void deleteById_removes_entity() {
        var saved = repo.save(sample());
        Long id = saved.getId();
        repo.deleteById(id);
        assertThat(repo.findById(id)).isEmpty();
    }
}