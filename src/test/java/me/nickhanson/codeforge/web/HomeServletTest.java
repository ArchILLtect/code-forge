package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServletTest {

    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;
    @Mock RequestDispatcher rd;
    @Mock QuoteService quoteService;
    @Mock ServletContext ctx;

    HomeServlet servlet = new HomeServlet();

    ServletConfig config;

    @BeforeEach
    void setup() throws Exception {
        lenient().when(req.getRequestDispatcher(any())).thenReturn(rd);
        lenient().when(req.getServletContext()).thenReturn(ctx);

        // inject mock QuoteService into servlet via reflection (field is private final)
        Field f = HomeServlet.class.getDeclaredField("quotes");
        f.setAccessible(true);
        f.set(servlet, quoteService);

        config = new ServletConfig() {
            @Override public String getServletName() { return "HomeServletTest"; }
            @Override public ServletContext getServletContext() { return ctx; }
            @Override public String getInitParameter(String name) { return null; }
            @Override public Enumeration<String> getInitParameterNames() { return Collections.emptyEnumeration(); }
        };
        servlet.init(config);
    }

    @Test
    void get_forwards_andSetsQuote() throws Exception {
        when(quoteService.getRandomQuote()).thenReturn("hello");
        servlet.doGet(req, resp);
        verify(req).setAttribute("quote", "hello");
        verify(req).getRequestDispatcher("/WEB-INF/jsp/home.jsp");
    }
}
