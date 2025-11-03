package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.service.ChallengeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengesServletTest {

    @Mock ServletContext ctx;
    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;
    @Mock RequestDispatcher rd;
    @Mock ChallengeService service;

    @InjectMocks ChallengesServlet servlet;

    ServletConfig config;

    @BeforeEach
    void setup() throws Exception {
        lenient().when(req.getServletContext()).thenReturn(ctx);
        lenient().when(ctx.getAttribute("challengeService")).thenReturn(service);
        lenient().when(req.getRequestDispatcher(any())).thenReturn(rd);

        // Create a minimal ServletConfig that returns our mocked ServletContext
        config = new ServletConfig() {
            @Override public String getServletName() { return "ChallengesServletTest"; }
            @Override public ServletContext getServletContext() { return ctx; }
            @Override public String getInitParameter(String name) { return null; }
            @Override public Enumeration<String> getInitParameterNames() { return Collections.emptyEnumeration(); }
        };
        servlet.init(config);
    }

    @Test
    void get_list_forwardsToListJsp_withChallenges() throws Exception {
        when(req.getPathInfo()).thenReturn(null);
        when(req.getParameter("difficulty")).thenReturn(null);
        when(service.listChallenges(null)).thenReturn(List.of(
                new Challenge("A", Difficulty.EASY, "", ""),
                new Challenge("B", Difficulty.MEDIUM, "", "")
        ));

        servlet.doGet(req, resp);

        verify(req).setAttribute(eq("challenges"), any());
        verify(rd).forward(req, resp);
    }

    @Test
    void get_detail_nonexistent_returns404() throws Exception {
        when(req.getPathInfo()).thenReturn("/999");
        when(service.getById(999L)).thenReturn(Optional.empty());

        servlet.doGet(req, resp);

        verify(resp).sendError(404);
    }

    @Test
    void post_create_redirectsOnSuccess() throws Exception {
        when(req.getPathInfo()).thenReturn(null);
        when(req.getParameter("title")).thenReturn("X");
        when(req.getParameter("blurb")).thenReturn("b");
        when(req.getParameter("difficulty")).thenReturn("EASY");
        when(req.getParameter("promptMd")).thenReturn("p");

        Challenge saved = new Challenge("X", Difficulty.EASY, "b", "p");
        saved.setId(123L);
        when(service.create(any())).thenReturn(saved);

        servlet.doPost(req, resp);

        verify(resp).sendRedirect(contains("/challenges/123"));
    }

    @Test
    void post_update_malformedPath_returns400() throws Exception {
        // choose a path that parses to a valid id but no known action -> final sendError(400)
        when(req.getPathInfo()).thenReturn("/123/unknown");

        servlet.doPost(req, resp);

        verify(resp).sendError(400);
    }
}
