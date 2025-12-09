package me.nickhanson.codeforge.testutil;

import me.nickhanson.codeforge.persistence.SessionFactoryProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Utility to purge core tables before tests to avoid unique constraint collisions
 * when using a persistent MySQL test database.
 */
public final class TestDbCleaner {
    private TestDbCleaner() {}

    public static void purgeCoreTables() {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // Order matters due to FKs
            session.createNativeQuery("DELETE FROM submissions").executeUpdate();
            session.createNativeQuery("DELETE FROM drill_items").executeUpdate();
            session.createNativeQuery("DELETE FROM challenges").executeUpdate();
            tx.commit();
        }
    }
}

