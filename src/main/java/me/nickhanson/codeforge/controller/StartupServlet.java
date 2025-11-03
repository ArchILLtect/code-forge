package me.nickhanson.codeforge.controller;

import me.nickhanson.codeforge.config.PropertiesLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Properties;

/**
 * ServletContextListener that loads Cognito properties at application startup
 * and makes them available as a servlet context attribute.
 * @author Nick Hanson
 */
@WebListener
public class StartupServlet implements ServletContextListener, PropertiesLoader {

    /**
     * Loads Cognito properties and sets them as a servlet context attribute.
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Properties props = loadProperties("/cognito.properties");
        sce.getServletContext().setAttribute("cognitoProperties", props);
    }

    /**
     * No operation on context destruction.
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }
}
