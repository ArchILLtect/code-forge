package me.nickhanson.codeforge.utilities;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChallengeRepoTest {

    @Autowired
    private ChallengeRepo repo;

    private Challenge sample() {
        return new Challenge(
                "Two Sum",
                Difficulty.EASY,
                "Find indices of two numbers that add up to target.",
                "## Prompt\nGiven an array nums and an integer target..."
        );
    }

    @Test
    void save_and_findById_success() {
        var saved = repo.save(sample());
        assertThat(saved.getId()).isNotNull();

        var found = repo.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Two Sum");
        assertThat(found.get().getDifficulty()).isEqualTo(Difficulty.EASY);
    }

    @Test
    void findAll_returns_list() {
        repo.save(sample());
        repo.save(new Challenge("Reverse String", Difficulty.EASY, "Reverse characters", "details..."));
        List<Challenge> all = repo.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void findByTitle_missing_returns_empty() {
        assertThat(repo.findByTitle("Nope")).isEmpty();
    }

    @Test
    void deleteById_removes_entity() {
        var saved = repo.save(sample());
        Long id = saved.getId();
        repo.deleteById(id);
        assertThat(repo.findById(id)).isEmpty();
    }
}