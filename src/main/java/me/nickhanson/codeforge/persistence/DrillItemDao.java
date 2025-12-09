package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.DrillItem;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * DAO for DrillItem entity.
 * Provides generic CRUD operations and entity-specific queries.
 * @author Nick Hanson
 */
public class DrillItemDao {
    private final GenericDao<DrillItem> data = new GenericDao<>(DrillItem.class);

    // ---- generic CRUD (delegated) ----
    public DrillItem getById(Long id) { return data.getById(id); }
    public List<DrillItem> getAll() { return data.getAll(); }
    public void saveOrUpdate(DrillItem item) { data.saveOrUpdate(item); }
    public void delete(DrillItem item) { data.delete(item); }

    // ---- entity-specific queries ----

    /**
     * List all DrillItems for a given Challenge ID, ordered by nextDueAt ascending.
     * @param challengeId the ID of the Challenge
     * @return list of DrillItems associated with the Challenge
     */
    public List<DrillItem> listByChallengeId(Long challengeId) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<DrillItem> results = (List<DrillItem>) s.createQuery(
                            "select d from DrillItem d join fetch d.challenge c where c.id = :cid order by d.nextDueAt asc")
                    .setParameter("cid", challengeId)
                    .getResultList();
            return results;
        }
    }

    /**
     * List all DrillItems that are due at or before the specified time.
     * @param now the cutoff Instant for due items
     * @return list of due DrillItems
     */
    public List<DrillItem> dueNow(Instant now) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<DrillItem> results = (List<DrillItem>) s.createQuery(
                            "select d from DrillItem d join fetch d.challenge where d.nextDueAt <= :now order by d.nextDueAt asc")
                    .setParameter("now", now)
                    .getResultList();
            return results;
        }
    }

    /**
     * Count of DrillItems that are due at or before the specified time.
     * @param now the cutoff Instant for due items
     * @return count of due DrillItems
     */
    public long countDue(Instant now) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            Long count = s.createQuery(
                            "select count(d) from DrillItem d where d.nextDueAt <= :now", Long.class)
                    .setParameter("now", now)
                    .getSingleResult();
            return (count != null ? count : 0L);
        }
    }

    /**
     * Bulk reschedule DrillItems by setting their nextDueAt to the specified time.
     * @param ids list of DrillItem IDs to reschedule
     * @param nextDueAt the new nextDueAt Instant
     * @return number of items updated
     */
    public int bulkReschedule(List<Long> ids, Instant nextDueAt) {
        if (ids == null || ids.isEmpty()) return 0;

        Session s = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            int updated = s.createQuery(
                            "update DrillItem d set d.nextDueAt = :when where d.id in (:ids)")
                    .setParameter("when", nextDueAt)
                    .setParameterList("ids", ids)
                    .executeUpdate();
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * List of due DrillItems up to the specified limit, ordered by nextDueAt.
     * @param now the cutoff Instant for due items
     * @param limit maximum number of items to return
     * @return list of due DrillItems
     */
    public List<DrillItem> dueQueue(Instant now, int limit) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        try {
            @SuppressWarnings("unchecked")
            List<DrillItem> results = (List<DrillItem>) session.createQuery(
                            "select d from DrillItem d join fetch d.challenge where d.nextDueAt is null or d.nextDueAt <= :now " +
                                    "order by case when d.nextDueAt is null then 0 else 1 end asc, d.nextDueAt asc")
                    .setParameter("now", now)
                    .setMaxResults(limit)
                    .getResultList();
            return results;
        } finally {
            session.close();
        }
    }

    /**
     * Find the DrillItem with the soonest upcoming nextDueAt.
     * @return Optional containing the soonest upcoming DrillItem, or empty if none exist
     */
    public Optional<DrillItem> soonestUpcoming() {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        try {
            @SuppressWarnings("unchecked")
            List<DrillItem> list = (List<DrillItem>) session.createQuery(
                            "select d from DrillItem d join fetch d.challenge where d.nextDueAt is not null order by d.nextDueAt asc")
                    .setMaxResults(1)
                    .getResultList();

            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally {
            session.close();
        }
    }

    /**
     * List all DrillItems for a given Challenge ID and user, ordered by nextDueAt ascending.
     * @param challengeId the ID of the Challenge
     * @param userId the ID of the User
     * @return list of DrillItems associated with the Challenge and User
     */
    public List<DrillItem> listByChallengeIdAndUser(Long challengeId, String userId) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<DrillItem> results = (List<DrillItem>) s.createQuery(
                            "select d from DrillItem d join fetch d.challenge c where c.id = :cid and d.userId = :uid order by d.nextDueAt asc")
                    .setParameter("cid", challengeId)
                    .setParameter("uid", userId)
                    .getResultList();
            return results;
        }
    }

    /**
     * List all DrillItems that are due at or before the specified time for a specific user.
     * @param now the cutoff Instant for due items
     * @param userId the ID of the User
     * @return list of due DrillItems for the User
     */
    public List<DrillItem> dueNow(Instant now, String userId) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            @SuppressWarnings("unchecked")
            List<DrillItem> results = (List<DrillItem>) s.createQuery(
                            "select d from DrillItem d join fetch d.challenge where d.userId = :uid and d.nextDueAt <= :now order by d.nextDueAt asc")
                    .setParameter("uid", userId)
                    .setParameter("now", now)
                    .getResultList();
            return results;
        }
    }

    /**
     * Count of DrillItems that are due at or before the specified time for a specific user.
     * @param now the cutoff Instant for due items
     * @param userId the ID of the User
     * @return count of due DrillItems for the User
     */
    public long countDue(Instant now, String userId) {
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            Long count = s.createQuery(
                            "select count(d) from DrillItem d where d.userId = :uid and d.nextDueAt <= :now", Long.class)
                    .setParameter("uid", userId)
                    .setParameter("now", now)
                    .getSingleResult();
            return (count != null ? count : 0L);
        }
    }

    /**
     * User-scoped due queue up to the specified limit, ordered by nextDueAt.
     * @param now the cutoff Instant for due items
     * @param limit maximum number of items to return
     * @param userId the ID of the User
     * @return list of due DrillItems for the User
     */
    public List<DrillItem> dueQueue(Instant now, int limit, String userId) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        try {
            @SuppressWarnings("unchecked")
            List<DrillItem> results = (List<DrillItem>) session.createQuery(
                            "select d from DrillItem d join fetch d.challenge where d.userId = :uid and (d.nextDueAt is null or d.nextDueAt <= :now) " +
                                    "order by case when d.nextDueAt is null then 0 else 1 end asc, d.nextDueAt asc")
                    .setParameter("uid", userId)
                    .setParameter("now", now)
                    .setMaxResults(limit)
                    .getResultList();
            return results;
        } finally {
            session.close();
        }
    }

    /**
     * Find the soonest upcoming DrillItem for a user.
     * @param userId the ID of the User
     * @return Optional containing the soonest upcoming DrillItem for the User, or empty if none exist
     */
    public Optional<DrillItem> soonestUpcoming(String userId) {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        try {
            @SuppressWarnings("unchecked")
            List<DrillItem> list = (List<DrillItem>) session.createQuery(
                            "select d from DrillItem d join fetch d.challenge where d.userId = :uid and d.nextDueAt is not null order by d.nextDueAt asc")
                    .setParameter("uid", userId)
                    .setMaxResults(1)
                    .getResultList();

            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally {
            session.close();
        }
    }
}