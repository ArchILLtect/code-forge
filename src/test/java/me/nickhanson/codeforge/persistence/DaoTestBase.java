package me.nickhanson.codeforge.persistence;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Statement;

/**
 * Base class for DAO integration tests. It resets the in-memory H2 database tables
 * between tests for isolation.
 */
public abstract class DaoTestBase {

    @BeforeEach
    void resetDatabase() {
        // Delete in FK-safe order
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            session.doWork(conn -> {
                try (Statement st = conn.createStatement()) {
                    st.execute("SET REFERENTIAL_INTEGRITY TO FALSE");
                    st.execute("TRUNCATE TABLE SUBMISSIONS RESTART IDENTITY");
                    st.execute("TRUNCATE TABLE DRILL_ITEMS RESTART IDENTITY");
                    st.execute("TRUNCATE TABLE CHALLENGES RESTART IDENTITY");
                    st.execute("SET REFERENTIAL_INTEGRITY TO TRUE");
                }
            });
        }
    }

    @AfterEach
    void cleanUp() {
        // No-op for now; tables are already clean. Hook reserved for future use.
    }
}

