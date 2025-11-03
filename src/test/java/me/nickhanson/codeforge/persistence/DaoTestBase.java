package me.nickhanson.codeforge.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for DAO integration tests. It resets the in-memory H2 database tables
 * between tests for isolation.
 */
public abstract class DaoTestBase {

    @BeforeEach
    void resetDatabase() {
        // Delete in FK-safe order
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeQuery("DELETE FROM SUBMISSIONS").executeUpdate();
            session.createNativeQuery("DELETE FROM DRILL_ITEMS").executeUpdate();
            session.createNativeQuery("DELETE FROM CHALLENGES").executeUpdate();
            tx.commit();
        }
    }

    @AfterEach
    void cleanUp() {
        // No-op for now; tables are already clean. Hook reserved for future use.
    }
}

