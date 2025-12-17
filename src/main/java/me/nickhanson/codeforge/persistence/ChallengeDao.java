package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Data Access Object (DAO) for the Challenge entity.
 * Provides methods to perform CRUD operations and custom queries on Challenge entities.
 * @author Nick Hanson
 */
public class ChallengeDao {

    private static final Logger log = LogManager.getLogger(ChallengeDao.class);

    private final GenericDao<Challenge> data = new GenericDao<>(Challenge.class);

    // Constructor
    public ChallengeDao() {
        super();
        log.info("ChallengeDao using SessionFactory identity: {}",
                System.identityHashCode(SessionFactoryProvider.getSessionFactory()));
    }

    public Challenge getById(Long id)                  { return data.getById(id); }
    public List<Challenge> getAll()                    { return data.getAll(); }
    public void saveOrUpdate(Challenge challenge)      { data.saveOrUpdate(challenge); }
    public void delete(Challenge challenge)            { data.delete(challenge); }

    /**
     * Finds challenges by their title.
     * @param title The title of the challenge to search for.
     * @return A list of challenges matching the given title.
     */
    public List<Challenge> findByTitle(String title) {
        return data.findByPropertyEqual("title", title);
    }

    /**
     * Finds challenges by their difficulty level.
     * @param difficulty The difficulty level to search for.
     * @return A list of challenges matching the given difficulty level.
     */
    public List<Challenge> findByDifficulty(Difficulty difficulty) {
        return data.findByPropertyEqual("difficulty", difficulty);
    }


    /**
     * Checks if a challenge with the given title exists, ignoring case.
     * @param title The title to check for existence.
     * @return True if a challenge with the given title exists, false otherwise.
     */
    public boolean existsTitleIgnoreCase(String title) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Challenge> root = cq.from(Challenge.class);

            Predicate sameTitleIgnoreCase =
                    cb.equal(cb.lower(root.get("title")), title.toLowerCase());

            cq.select(cb.count(root)).where(sameTitleIgnoreCase);

            Long count = s.createQuery(cq).getSingleResult();
            return count != null && count > 0;
        }
    }

    /**
     * Checks if a challenge with the given title exists, ignoring case,
     * excluding a specific challenge by its ID.
     * @param title The title to check for existence.
     * @param excludeId The ID of the challenge to exclude from the check.
     * @return True if a challenge with the given title exists (excluding the specified ID), false otherwise.
     */
    public boolean existsTitleForOtherIgnoreCase(String title, Long excludeId) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Challenge> root = cq.from(Challenge.class);

            Predicate sameTitleIgnoreCase =
                    cb.equal(cb.lower(root.get("title")), title.toLowerCase());
            Predicate notSameId =
                    cb.notEqual(root.get("id"), excludeId);

            cq.select(cb.count(root)).where(cb.and(sameTitleIgnoreCase, notSameId));

            Long count = s.createQuery(cq).getSingleResult();
            return count != null && count > 0;
        }
    }
}