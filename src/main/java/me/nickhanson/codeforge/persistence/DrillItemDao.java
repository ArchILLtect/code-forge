package me.nickhanson.codeforge.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.nickhanson.codeforge.entity.DrillItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class DrillItemDao {

    @PersistenceContext
    private EntityManager em;

    public Optional<DrillItem> findById(Long id) {
        return Optional.ofNullable(em.find(DrillItem.class, id));
    }

    public List<DrillItem> findAll() {
        return em.createQuery("SELECT d FROM DrillItem d ORDER BY d.id ASC", DrillItem.class)
                .getResultList();
    }

    public List<DrillItem> findByChallengeId(Long challengeId) {
        return em.createQuery(
                        "SELECT d FROM DrillItem d WHERE d.challenge.id = :cid ORDER BY d.createdAt DESC",
                        DrillItem.class)
                .setParameter("cid", challengeId)
                .getResultList();
    }

    public boolean existsById(Long id) {
        return em.find(DrillItem.class, id) != null;
    }

    @Transactional
    public DrillItem save(DrillItem item) {
        if (item.getId() == null) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Transactional
    public boolean deleteById(Long id) {
        DrillItem found = em.find(DrillItem.class, id);
        if (found == null) return false;
        em.remove(found);
        return true;
    }
}