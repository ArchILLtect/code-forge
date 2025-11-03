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
 * This class initializes the SessionFactory using the configuration
 * @author Nick Hanson
 */
public class SessionFactoryProvider {
    private static final Logger log = LogManager.getLogger(SessionFactoryProvider.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the SessionFactory from hibernate.cfg.xml configuration file.
     * @return the initialized SessionFactory
     * TODO: simplify build post-issues
     */
    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry registry =
                    new StandardServiceRegistryBuilder()
                            .configure("hibernate.cfg.xml")
                            .build();

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
        } catch (Exception e) {
            log.error("SessionFactory initialization failed", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Gets the singleton SessionFactory instance.
     * @return the SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}