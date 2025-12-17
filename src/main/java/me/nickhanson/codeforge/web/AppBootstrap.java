package me.nickhanson.codeforge.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import me.nickhanson.codeforge.service.ChallengeRunService;
import me.nickhanson.codeforge.service.ChallengeService;
import me.nickhanson.codeforge.service.DrillService;
import me.nickhanson.codeforge.persistence.SessionFactoryProvider;
import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// TODO: Improve asset delivery (S3 pipeline) after course completion.

/**
 * Application bootstrap listener to initialize services and seed data.
 * Registers service singletons in the servlet context and seeds development data if necessary.
 * This class is automatically detected and invoked by the servlet container.
 * @author Nick Hanson
 */
@WebListener
public class AppBootstrap implements ServletContextListener {

    /**
     * Initialize the servlet context by creating service singletons and seeding data.
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        // Create service singletons
        ChallengeService challengeService = new ChallengeService();
        DrillService drillService = new DrillService();
        ChallengeRunService runService = new ChallengeRunService();

        // Register in context
        ctx.setAttribute("challengeService", challengeService);
        ctx.setAttribute("drillService", drillService);
        ctx.setAttribute("runService", runService);

        // Seed dev data if empty
        try (Session s = SessionFactoryProvider.getSessionFactory().openSession()) {
            Long count = (Long) s.createQuery("select count(c) from Challenge c").getSingleResult();
            if (count == null || count == 0) {
                try (InputStream in = getClass().getClassLoader().getResourceAsStream("data.sql")) {
                    if (in != null) {
                        StringBuilder sb = new StringBuilder();
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                            br.lines().forEach(line -> sb.append(line).append('\n'));
                        }
                        String sql = sb.toString();
                        s.beginTransaction();
                        for (String stmt : sql.split(";\s*\n")) {
                            String t = stmt.trim();
                            if (!t.isEmpty()) {
                                s.createNativeQuery(t).executeUpdate();
                            }
                        }
                        s.getTransaction().commit();
                    }
                }
            }
        } catch (Exception e) {
            ctx.log("Seeding failed: " + e.getMessage(), e);
        }
    }
}
