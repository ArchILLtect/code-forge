package me.nickhanson.codeforge.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * H2 TCP server listener disabled for MVP (MySQL-only).
 * Left as a no-op to avoid classpath churn; can be removed post-MVP.
 */
@WebListener
public class H2TcpServerListener implements ServletContextListener {
    private static final Logger log = LogManager.getLogger(H2TcpServerListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("H2TcpServerListener: disabled (MySQL-only MVP)");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }
}