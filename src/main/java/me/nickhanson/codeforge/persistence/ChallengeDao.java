package me.nickhanson.codeforge.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class ChallengeDao {

    @PersistenceContext
    private EntityManager em;

    public Optional<Challenge> findById(Long id) {
        return Optional.ofNullable(em.find(Challenge.class, id));
    }

    public List<Challenge> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
        Root<Challenge> root = cq.from(Challenge.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    public List<Challenge> findByDifficulty(Difficulty difficulty) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
        Root<Challenge> root = cq.from(Challenge.class);
        Predicate p = cb.equal(root.get("difficulty"), difficulty);
        cq.select(root).where(p);
        return em.createQuery(cq).getResultList();
    }

    public Optional<Challenge> findByTitle(String title) {
        List<Challenge> results = em.createQuery(
                        "SELECT c FROM Challenge c WHERE LOWER(c.title) = LOWER(:title)", Challenge.class)
                .setParameter("title", title)
                .setMaxResults(1)
                .getResultList();
        return results.stream().findFirst();
    }

    public boolean existsByTitleIgnoreCase(String title) {
        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Challenge c WHERE LOWER(c.title) = LOWER(:title)", Long.class)
                .setParameter("title", title)
                .getSingleResult();
        return count != null && count > 0;
    }

    public boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id) {
        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Challenge c WHERE LOWER(c.title) = LOWER(:title) AND c.id <> :id", Long.class)
                .setParameter("title", title)
                .setParameter("id", id)
                .getSingleResult();
        return count != null && count > 0;
    }

    public boolean existsById(Long id) {
        return em.find(Challenge.class, id) != null;
    }

    @Transactional
    public Challenge save(Challenge challenge) {
        if (challenge.getId() == null) {
            em.persist(challenge);
            return challenge;
        } else {
            return em.merge(challenge);
        }
    }

    @Transactional
    public boolean deleteById(Long id) {
        Challenge found = em.find(Challenge.class, id);
        if (found == null) return false;
        em.remove(found);
        return true;
    }
}