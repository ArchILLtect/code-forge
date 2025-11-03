package me.nickhanson.codeforge.controller;

import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.Mockito.*;

class StartupServletTest {

    @Test
    void contextInitialized_setsCognitoPropertiesAttribute() {
        ServletContext ctx = mock(ServletContext.class);
        ServletContextEvent event = new ServletContextEvent(ctx);

        StartupServlet s = new StartupServlet();
        s.contextInitialized(event);

        verify(ctx).setAttribute(eq("cognitoProperties"), any());
    }
}

