package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.web.ChallengeForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for managing Challenge entities.
 * Provides methods for CRUD operations and business logic.
 * @author Nick Hanson
 */
public class ChallengeService {

    private static final Logger log = LogManager.getLogger(ChallengeService.class);

    private final ChallengeDao dao;

    // Default constructor uses a new ChallengeDao instance.
    public ChallengeService() {
        this.dao = new ChallengeDao();
    }

    // Constructor for DI of a ChallengeDao.
    public ChallengeService(ChallengeDao dao) {
        this.dao = dao;
    }

    /**
     * Lists challenges, optionally filtered by difficulty.
     * @param difficulty The difficulty filter; null returns all.
     * @return List of challenges.
     */
    public List<Challenge> listChallenges(Difficulty difficulty) {
        List<Challenge> all = dao.getAll();
        if (difficulty == null) {
            return all;
        } else {
            return dao.findByDifficulty(difficulty);
        }
    }

    /**
     * Retrieves a challenge by its unique identifier.
     * @param id The unique identifier of the challenge.
     * @return An Optional containing the challenge if found, or empty if not found.
     */
    public Optional<Challenge> getById(Long id) {
        return Optional.ofNullable(dao.getById(id));
    }

    /**
     * Checks if a challenge title already exists (case-insensitive).
     * @param title The title to check.
     * @return true if the title exists, false otherwise.
     */
    public boolean titleExists(String title) {
        if (title == null || title.isBlank()) return false;
        return dao.existsTitleIgnoreCase(title);
    }

    /**
     * Checks for the existence of a challenge title in other records (case-insensitive).
     * @param title The title to check.
     * @param id The ID to exclude from the check.
     * @return true if the title exists in other records, false otherwise.
     */
    public boolean titleExistsForOther(String title, Long id) {
        if (title == null || title.isBlank() || id == null) return false;
        return dao.existsTitleForOtherIgnoreCase(title, id);
    }

    /**
     * Creates a new challenge using the provided form data.
     * @param form The form data for the new challenge.
     * @return The created Challenge entity.
     */
    public Challenge create(ChallengeForm form) {

        // Very simple validation
        Objects.requireNonNull(form, "form");
        Objects.requireNonNull(form.getTitle(), "title");
        Objects.requireNonNull(form.getDifficulty(), "difficulty");

        Challenge challenge = new Challenge(
                form.getTitle(),
                form.getDifficulty(),
                form.getBlurb(),
                form.getPromptMd()
        );
        // Set expected answer for evaluator (MVP)
        challenge.setExpectedAnswer(form.getExpectedAnswer());
        dao.saveOrUpdate(challenge);
        log.info("Created challenge id={} title='{}'", challenge.getId(), challenge.getTitle());
        return challenge;
    }

    /**
     * Updates an existing challenge with the provided form data.
     * @param id The ID of the challenge to update.
     * @param form The form data for the update.
     * @return An Optional containing the updated Challenge if found, or empty if not found.
     */
    public Optional<Challenge> update(Long id, ChallengeForm form) {
        Challenge existing = dao.getById(id);
        if (existing == null) return Optional.empty();

        existing.setTitle(form.getTitle());
        existing.setDifficulty(form.getDifficulty());
        existing.setBlurb(form.getBlurb());
        existing.setPromptMd(form.getPromptMd());
        // Update expected answer
        existing.setExpectedAnswer(form.getExpectedAnswer());
        dao.saveOrUpdate(existing);
        log.info("Updated challenge id={}", id);
        return Optional.of(existing);
    }

    /**
     * Deletes a challenge by its ID.
     * @param id The ID of the challenge to delete.
     * @return true if the challenge was deleted, false if not found.
     */
    public boolean delete(Long id) {
        Challenge existing = dao.getById(id);
        if (existing == null) return false;
        dao.delete(existing);
        log.info("Deleted challenge id={}", id);
        return true;
    }
}
