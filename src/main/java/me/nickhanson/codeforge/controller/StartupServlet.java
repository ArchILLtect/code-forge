package me.nickhanson.codeforge.controller;

import me.nickhanson.codeforge.config.PropertiesLoader;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Properties;

@WebListener
public class StartupServlet implements ServletContextListener, PropertiesLoader {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties props = loadProperties("/application.properties");
        sce.getServletContext().setAttribute("cognitoProperties", props);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }
}
