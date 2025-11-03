package me.nickhanson.codeforge.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Collections;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogOutTest {

    @Mock ServletContext ctx;
    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;
    @Mock HttpSession session;

    @InjectMocks
    LogOut servlet;

    ServletConfig config;

    @BeforeEach
    void setup() throws Exception {
        lenient().when(req.getServletContext()).thenReturn(ctx);
        lenient().when(req.getSession(false)).thenReturn(session);
        lenient().doNothing().when(session).invalidate();

        config = new ServletConfig() {
            @Override public String getServletName() { return "LogOutTest"; }
            @Override public ServletContext getServletContext() { return ctx; }
            @Override public String getInitParameter(String name) { return null; }
            @Override public Enumeration<String> getInitParameterNames() { return Collections.emptyEnumeration(); }
        };
        servlet.init();
    }

    @Test
    void doGet_invalidatesSession_andRedirects() throws Exception {
        servlet.doGet(req, resp);
        verify(session).invalidate();
        verify(resp).sendRedirect(anyString());
    }
}
