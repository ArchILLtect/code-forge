package me.nickhanson.codeforge.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.nickhanson.codeforge.entity.Submission;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class SubmissionDao {

    @PersistenceContext
    private EntityManager em;

    public Optional<Submission> findById(Long id) {
        return Optional.ofNullable(em.find(Submission.class, id));
    }

    public List<Submission> findAll() {
        return em.createQuery("SELECT s FROM Submission s ORDER BY s.id ASC", Submission.class)
                .getResultList();
    }

    public List<Submission> findByChallengeId(Long challengeId) {
        return em.createQuery(
                        "SELECT s FROM Submission s WHERE s.challenge.id = :cid ORDER BY s.createdAt DESC",
                        Submission.class)
                .setParameter("cid", challengeId)
                .getResultList();
    }

    public boolean existsById(Long id) {
        return em.find(Submission.class, id) != null;
    }

    @Transactional
    public Submission save(Submission submission) {
        if (submission.getId() == null) {
            em.persist(submission);
            return submission;
        } else {
            return em.merge(submission);
        }
    }

    @Transactional
    public boolean deleteById(Long id) {
        Submission found = em.find(Submission.class, id);
        if (found == null) return false;
        em.remove(found);
        return true;
    }
}