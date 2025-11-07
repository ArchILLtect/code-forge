package me.nickhanson.codeforge.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

/**
 * Generic, concrete data helper used by DAOs to perform common CRUD operations
 * with Hibernate SessionFactory. This is intentionally not a Spring bean to
 * match the class pattern used in the project.
 * @author Nick Hanson
 */
public class EntityData<T> {

    private static final Logger log = LogManager.getLogger(EntityData.class);

    // The entity class type managed by this instance
    private final Class<T> type;
    // Hibernate SessionFactory for database operations
    private final SessionFactory sessionFactory;

    // Constructor accepting the entity class type
    public EntityData(Class<T> type) {
        this.type = Objects.requireNonNull(type, "type");
        this.sessionFactory = SessionFactoryProvider.getSessionFactory();
    }

    /**
     * Get entity by its primary key ID.
     * @param id the primary key ID
     * @return the entity instance or null if not found
     */
    public T getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(type, id);
        }
    }

    /**
     * Get all entities of type T, ordered by ID ascending.
     * @return list of all entities
     */
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            cq.select(root).orderBy(cb.asc(root.get("id"))); // assumes field "id"
            return session.createQuery(cq).getResultList();
        }
    }

    /**
     * Save or update the given entity.
     * @param entity the entity to save or update
     */
    public void saveOrUpdate(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(entity);         // Hibernate API, in-scope
            tx.commit();
        } catch (RuntimeException ex) {
            rollbackQuietly(tx);
            throw ex;
        }
    }

    /**
     * Delete the given entity.
     * @param entity the entity to delete
     */
    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            // ensure the instance is managed before remove
            T managed = entity;
            if (!session.contains(entity)) {
                managed = session.merge(entity);
            }
            session.remove(managed);
            tx.commit();
        } catch (Exception e) {
            rollbackQuietly(tx);
            log.error("delete failed for {}", type.getSimpleName(), e);
            throw e;
        }
    }

    private void rollbackQuietly(Transaction tx) {
        if (tx != null) {
            try {
                tx.rollback();
            } catch (Exception e) {
                log.warn("Transaction rollback failed", e);
            }
        }
    }


    /**
     * Find entities where a property equals a given value.
     * @param property the property name
     * @param value the value to match
     * @return list of matching entities
     */
    public List<T> findByPropertyEqual(String property, Object value) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            cq.select(root)
                    .where(cb.equal(root.get(property), value))
                    .orderBy(cb.asc(root.get("id"))); // assumes field "id"
            return session.createQuery(cq).getResultList();
        }
    }
}
