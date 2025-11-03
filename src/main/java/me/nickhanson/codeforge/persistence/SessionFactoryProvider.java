package me.nickhanson.codeforge.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a singleton SessionFactory for Hibernate ORM.
 * This class initializes the SessionFactory using the configuration
 * @author Nick Hanson
 */
public class SessionFactoryProvider {
    private static final Logger log = LogManager.getLogger(SessionFactoryProvider.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the SessionFactory from hibernate.cfg.xml configuration file.
     * Falls back to a programmatic in-memory H2 configuration if XML loading fails
     * (useful in CI when XML DTD/network issues occur).
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Preferred: load from hibernate.cfg.xml on classpath
            StandardServiceRegistry registry =
                    new StandardServiceRegistryBuilder()
                            .configure("hibernate.cfg.xml")
                            .build();

            return buildFromRegistry(registry);
        } catch (Exception xmlErr) {
            log.warn("Failed to load hibernate.cfg.xml; falling back to programmatic H2 config for tests. Cause: {}",
                    xmlErr.toString(), xmlErr);
            try {
                Map<String, Object> settings = new HashMap<>();
                settings.put("hibernate.connection.driver_class", "org.h2.Driver");
                settings.put("hibernate.connection.url", "jdbc:h2:mem:codeforge_ci;DB_CLOSE_DELAY=-1");
                settings.put("hibernate.connection.username", "sa");
                settings.put("hibernate.connection.password", "");
                settings.put("hibernate.hbm2ddl.auto", "update");
                settings.put("hibernate.show_sql", "false");
                settings.put("hibernate.format_sql", "false");
                settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .applySettings(settings)
                        .build();
                return buildFromRegistry(registry);
            } catch (Exception e) {
                log.error("SessionFactory initialization failed (fallback path)", e);
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private static SessionFactory buildFromRegistry(StandardServiceRegistry registry) {
        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.Challenge.class)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.Submission.class)
                .addAnnotatedClass(me.nickhanson.codeforge.entity.DrillItem.class);

        Metadata metadata = sources.buildMetadata();
        SessionFactory sf = metadata.buildSessionFactory();

        // log mapped entities
        sf.getMetamodel().getEntities().forEach(e ->
                log.info("Mapped entity -> name='{}' javaType={}", e.getName(), e.getJavaType())
        );

        log.info("SessionFactory identity: {}", System.identityHashCode(sf));
        return sf;
    }

    /**
     * Gets the singleton SessionFactory instance.
     * @return the SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}