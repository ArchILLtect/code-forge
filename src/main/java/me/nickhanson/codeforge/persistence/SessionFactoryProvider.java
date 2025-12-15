package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.config.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Provides a singleton SessionFactory for Hibernate ORM.
 * Resolution order for config values:
 *   1) System properties        (e.g. -Dhibernate.connection.url=...)
 *   2) Environment variables    (e.g. DB_HOST, DB_USER, DB_PASS, ...)
 *   3) local.properties         (on classpath, git-ignored; for local dev)
 *   4) Hard-coded default       (only for non-secret things)
 */
public class SessionFactoryProvider {

    private static final Logger log = LogManager.getLogger(SessionFactoryProvider.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Resolve a config value in this order:
     *   - Java system property
     *   - Environment variable
     *   - local.properties (via LocalConfig)
     *   - Provided default
     */
    private static String resolve(String key, String defaultVal) {
        String fromSys = System.getProperty(key);
        if (fromSys != null && !fromSys.isBlank()) {
            return fromSys;
        }

        String fromEnv = System.getenv(key);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }

        String fromLocal = LocalConfig.get(key);
        if (fromLocal != null && !fromLocal.isBlank()) {
            return fromLocal;
        }

        return defaultVal;
    }

    /**
     * Build the SessionFactory using resolved configuration values.
     */
    private static SessionFactory buildSessionFactory() {
        // URL: explicit hibernate prop wins; otherwise build from DB_HOST/DB_PORT/DB_NAME
        String explicitUrl = resolve("hibernate.connection.url", null);
        String url;

        if (explicitUrl != null && !explicitUrl.isBlank()) {
            url = explicitUrl;
        } else {
            String host = resolve("DB_HOST", "localhost");
            String port = resolve("DB_PORT", "3306");
            String db   = resolve("DB_NAME", "cf_test_db");

            url = "jdbc:mysql://" + host + ":" + port + "/" + db
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        }

        // Username: hibernate prop -> DB_USER -> default "root"
        String user = resolve("hibernate.connection.username", null);
        if (user == null || user.isBlank()) {
            user = resolve("DB_USER", "root");
        }

        // Password: hibernate prop -> DB_PASS -> DB_PASSWORD
        String pass = resolve("hibernate.connection.password", null);
        if (pass == null || pass.isBlank()) {
            pass = resolve("DB_PASS", null);
        }
        if (pass == null || pass.isBlank()) {
            pass = resolve("DB_PASSWORD", null);
        }
        if (pass == null || pass.isBlank()) {
            throw new IllegalStateException(
                    "No DB password configured. Tried hibernate.connection.password, "
                            + "DB_PASS, DB_PASSWORD, and local.properties."
            );
        }

        // Dialect: can still be overridden if needed
        String dialect = resolve(
                "hibernate.dialect",
                "org.hibernate.dialect.MySQL8Dialect"
        );

        // Ensure MySQL JDBC driver is present
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver not found on classpath", e);
        }

        // Log *non-secret* connection info
        log.info("Building SessionFactory with URL={} and user={}", url, user);

        // Build registry using XML then override with resolved settings
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .applySetting("hibernate.connection.url", url)
                .applySetting("hibernate.connection.username", user)
                .applySetting("hibernate.connection.password", pass)
                .applySetting("hibernate.dialect", dialect);

        StandardServiceRegistry registry = builder.build();
        return buildFromRegistry(registry);
    }

    /**
     * Build SessionFactory from the given StandardServiceRegistry.
     */
    private static SessionFactory buildFromRegistry(StandardServiceRegistry registry) {
        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.Challenge.class)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.Submission.class)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.DrillItem.class);

        Metadata metadata = sources.buildMetadata();
        return metadata.buildSessionFactory();
    }

    /**
     * Gets the singleton SessionFactory instance.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}