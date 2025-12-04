package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Submission;
import org.hibernate.Session;

import java.util.List;

/**
 * Data Access Object for Submission entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Nick Hanson
 */
public class SubmissionDao {
    private final GenericDao<Submission> data = new GenericDao<>(Submission.class);

    // CRUD Operations
    public Submission getById(Long id) { return data.getById(id); }
    public List<Submission> getAll()   { return data.getAll(); }
    public void saveOrUpdate(Submission submission) { data.saveOrUpdate(submission); }
    public void delete(Submission submission) { data.delete(submission); }

    /**
     * Lists all submissions associated with a specific challenge ID.
     * @param challengeId The ID of the challenge.
     * @return A list of submissions for the given challenge ID.
     */
    public List<Submission> listByChallengeId(Long challengeId) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            return s.createQuery(
                    "select s from Submission s join s.challenge c where c.id = :cid",
                    Submission.class)
                .setParameter("cid", challengeId)
                .getResultList();
        }
    }
}