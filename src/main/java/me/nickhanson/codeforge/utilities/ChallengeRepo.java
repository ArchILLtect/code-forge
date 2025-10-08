package me.nickhanson.codeforge.utilities;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Challenge entities.
 * Provides methods for CRUD operations and custom queries.
 */
public interface ChallengeRepo extends JpaRepository<Challenge, Long> {
    /**
     * Find a challenge by its title.
     *
     * @param title The title of the challenge
     * @return An Optional containing the Challenge if found, or empty if not found
     */
    Optional<Challenge> findByTitle(String title);

    /**
     * Retrieves a paginated list of challenges filtered by difficulty.
     *
     * @param difficulty The difficulty level to filter by.
     * @param pageable   The pagination and sorting information.
     * @return A page of challenges matching the specified difficulty.
     */
    Page<Challenge> findByDifficulty(Difficulty difficulty, Pageable pageable);

    /**
     * Checks if a challenge with the given title exists (case-insensitive).
     *
     * @param title The title to check for.
     * @return True if a challenge with the title exists, false otherwise.
     */
    boolean existsByTitleIgnoreCase(String title);

    /**
     * Checks if a challenge with the given title exists (case-insensitive), excluding a specific challenge by ID.
     *
     * @param title The title to check for.
     * @param id    The ID of the challenge to exclude from the check.
     * @return True if a challenge with the title exists (excluding the specified ID), false otherwise.
     */
    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);
}
