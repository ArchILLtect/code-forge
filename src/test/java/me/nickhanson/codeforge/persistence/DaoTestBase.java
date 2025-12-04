package me.nickhanson.codeforge.persistence;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Statement;

/**
 * Base class for DAO integration tests. Resets MySQL tables between tests for isolation.
 * MySQL-only for MVP (no H2).
 */
public abstract class DaoTestBase {

    @BeforeEach
    void resetDatabase() {
        // Delete in FK-safe way for MySQL
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            session.doWork(conn -> {
                try (Statement st = conn.createStatement()) {
                    st.execute("SET FOREIGN_KEY_CHECKS = 0");
                    // Use physical table names as created by Hibernate on MySQL
                    st.execute("TRUNCATE TABLE SUBMISSIONS");
                    st.execute("TRUNCATE TABLE drill_items");
                    st.execute("TRUNCATE TABLE CHALLENGES");
                    st.execute("SET FOREIGN_KEY_CHECKS = 1");
                }
            });
        }
    }

    @AfterEach
    void cleanUp() {
        // No-op for now; tables are already clean. Hook reserved for future use.
    }
}