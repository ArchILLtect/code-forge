package me.nickhanson.codeforge.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTest {

    Auth servlet;

    @Mock ServletContext ctx;
    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;
    @Mock RequestDispatcher rd;

    @BeforeEach
    void setup() throws Exception {
        servlet = new Auth();
        // Provide a simple ServletConfig via init (Auth.init reads properties and may fail if secret missing)
        ServletConfig cfg = new ServletConfig() {
            @Override public String getServletName() { return "AuthTest"; }
            @Override public ServletContext getServletContext() { return ctx; }
            @Override public String getInitParameter(String name) { return null; }
            @Override public java.util.Enumeration<String> getInitParameterNames() { return java.util.Collections.emptyEnumeration(); }
        };
        // prevent NPE on forward
        lenient().when(req.getRequestDispatcher(anyString())).thenReturn(rd);
        // initialize servlet with config; ignore ServletException if missing secret etc
        try {
            servlet.init(cfg);
        } catch (ServletException se) {
            // ignore init failures in test environment
        }
    }

    @Test
    void doGet_missingCode_forwardsToError() throws Exception {
        when(req.getParameter("code")).thenReturn(null);
        servlet.doGet(req, resp);
        verify(req).getRequestDispatcher("/WEB-INF/jsp/error/500.jsp");
    }
}
