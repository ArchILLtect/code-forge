package me.nickhanson.codeforge.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Provides a singleton SessionFactory for Hibernate ORM.
 * Loads configuration from hibernate.cfg.xml (MySQL only for MVP),
 * applying overrides from system properties or environment variables when present.
 */
public class SessionFactoryProvider {
    private static final Logger log = LogManager.getLogger(SessionFactoryProvider.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static String env(String key, String defVal) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : defVal;
    }

    private static String prop(String key, String defVal) {
        String v = System.getProperty(key);
        return (v != null && !v.isBlank()) ? v : defVal;
    }

    /**
     * Builds the SessionFactory from hibernate.cfg.xml configuration file.
     */
    private static SessionFactory buildSessionFactory() {
        // Resolve optional overrides
        String url = prop("hibernate.connection.url", null);
        if (url == null) {
            String host = env("DB_HOST", "localhost");
            String port = env("DB_PORT", "3306");
            String db   = env("DB_NAME", "cf_test_db");
            url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        }
        String user = prop("hibernate.connection.username", env("DB_USER", "root"));
        String pass = prop("hibernate.connection.password", env("DB_PASSWORD", ""));
        String dialect = prop("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        // Ensure MySQL JDBC driver is registered
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver not found on classpath", e);
        }

        // Build registry using XML, then apply non-empty overrides
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml");
        if (url != null && !url.isBlank()) {
            builder.applySetting("hibernate.connection.url", url);
        }
        if (user != null && !user.isBlank()) {
            builder.applySetting("hibernate.connection.username", user);
        }
        if (pass != null && !pass.isBlank()) {
            builder.applySetting("hibernate.connection.password", pass);
        }
        if (dialect != null && !dialect.isBlank()) {
            builder.applySetting("hibernate.dialect", dialect);
        }

        StandardServiceRegistry registry = builder.build();
        return buildFromRegistry(registry);
    }

    private static SessionFactory buildFromRegistry(StandardServiceRegistry registry) {
        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.Challenge.class)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.Submission.class)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.DrillItem.class);

        Metadata metadata = sources.buildMetadata();
        SessionFactory sf = metadata.buildSessionFactory();
        return sf;
    }

    /**
     * Gets the singleton SessionFactory instance.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}