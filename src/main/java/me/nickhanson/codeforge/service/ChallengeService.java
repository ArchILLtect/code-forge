package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.utilities.ChallengeRepo;
import me.nickhanson.codeforge.web.ChallengeForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing Challenge entities.
 * Provides methods for CRUD operations and business logic.
 */
@Service
public class ChallengeService {
    private final ChallengeRepo repo;

    /**
     * Constructor for ChallengeService.
     *
     * @param repo The repository used for accessing Challenge data.
     */
    public ChallengeService(ChallengeRepo repo) {
        this.repo = repo;
    }

    /**
     * Retrieves a paginated list of challenges, optionally filtered by difficulty.
     *
     * @param difficulty The difficulty level to filter by (can be null for no filtering).
     * @param pageable   The pagination and sorting information.
     * @return A page of challenges matching the criteria.
     */
    public org.springframework.data.domain.Page<Challenge> listChallenges(me.nickhanson.codeforge.entity.Difficulty difficulty, org.springframework.data.domain.Pageable pageable) {
        if (difficulty == null) {
            return repo.findAll(pageable);
        }
        return repo.findByDifficulty(difficulty, pageable);
    }

    /**
     * Retrieves a challenge by its unique identifier.
     *
     * @param id The unique identifier of the challenge.
     * @return An Optional containing the challenge if found, or empty if not found.
     */
    public Optional<Challenge> getById(Long id) {
        return repo.findById(id);
    }

    /**
     * Checks if a challenge with the given title exists.
     *
     * @param title The title to check for existence.
     * @return True if a challenge with the title exists, false otherwise.
     */
    public boolean titleExists(String title) {
        return repo.existsByTitleIgnoreCase(title);
    }

    /**
     * Checks if a challenge with the given title exists, excluding a specific challenge by ID.
     *
     * @param title The title to check for.
     * @param id    The ID of the challenge to exclude from the check.
     * @return True if a challenge with the title exists for another challenge, false otherwise.
     */
    public boolean titleExistsForOther(String title, Long id) {
        return repo.existsByTitleIgnoreCaseAndIdNot(title, id);
    }

    /**
     * Creates a new challenge based on the provided form data.
     *
     * @param form The form containing the challenge data.
     * @return The created Challenge entity.
     */
    @Transactional
    public Challenge create(ChallengeForm form) {
        Challenge c = new Challenge(form.getTitle(), form.getDifficulty(), form.getBlurb(), form.getPromptMd());
        return repo.save(c);
    }

    /**
     * Updates an existing challenge with the given ID using the provided form data.
     *
     * @param id   The ID of the challenge to update.
     * @param form The form containing the updated challenge data.
     * @return An Optional containing the updated challenge if found, or empty if not.
     */
    @Transactional
    public Optional<Challenge> update(Long id, ChallengeForm form) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(form.getTitle());
            existing.setDifficulty(form.getDifficulty());
            existing.setBlurb(form.getBlurb());
            existing.setPromptMd(form.getPromptMd());
            return repo.save(existing);
        });
    }

    /**
     * Deletes a challenge by its ID.
     *
     * @param id The ID of the challenge to delete.
     * @return True if the challenge was deleted, false if it was not found.
     */
    @Transactional
    public boolean delete(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
