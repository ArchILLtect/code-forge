package me.nickhanson.codeforge.controller;

import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;

import static org.mockito.Mockito.*;

class StartupServletTest {

    /**
     * Verifies that contextInitialized sets the "cognitoProperties" attribute in the ServletContext.
     */
    @Test
    void contextInitialized_setsCognitoPropertiesAttribute() {
        ServletContext ctx = mock(ServletContext.class);
        ServletContextEvent event = new ServletContextEvent(ctx);

        StartupServlet s = new StartupServlet();
        s.contextInitialized(event);

        verify(ctx).setAttribute(eq("cognitoProperties"), any());
    }
}


