package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.DrillItem;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.Outcome;
import me.nickhanson.codeforge.service.ChallengeRunService;
import me.nickhanson.codeforge.service.ChallengeService;
import me.nickhanson.codeforge.service.DrillService;
import me.nickhanson.codeforge.service.RunResult;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrillServletTest {

    @Mock ServletContext ctx;
    @Mock HttpServletRequest req;
    @Mock HttpServletResponse resp;
    @Mock RequestDispatcher rd;

    @Mock DrillService drillService;
    @Mock ChallengeService challengeService;
    @Mock ChallengeRunService runService;

    @InjectMocks DrillServlet servlet;

    ServletConfig config;

    /**
     * Setup before each test: initialize servlet with mocked context and services.
     */
    @BeforeEach
    void setup() throws Exception {
        lenient().when(req.getServletContext()).thenReturn(ctx);
        lenient().when(ctx.getAttribute("drillService")).thenReturn(drillService);
        lenient().when(ctx.getAttribute("challengeService")).thenReturn(challengeService);
        lenient().when(ctx.getAttribute("runService")).thenReturn(runService);
        lenient().when(req.getRequestDispatcher(any())).thenReturn(rd);
        var session = mock(javax.servlet.http.HttpSession.class);
        lenient().when(req.getSession(false)).thenReturn(session); // UserContext reads this
        lenient().when(req.getSession()).thenReturn(session);
        when(session.getAttribute("userSub")).thenReturn("demo-user");
        lenient().when(req.getContextPath()).thenReturn(""); // Avoid null in redirects

        config = new ServletConfig() {
            @Override public String getServletName() { return "DrillServletTest"; }
            @Override public ServletContext getServletContext() { return ctx; }
            @Override public String getInitParameter(String name) { return null; }
            @Override public Enumeration<String> getInitParameterNames() { return Collections.emptyEnumeration(); }
        };
        servlet.init(config);
    }

    /**
     * Tests that a GET to the drill endpoint forwards with due drill items.
     */
    @Test
    void get_queue_forwardsWithRows() throws Exception {
        when(req.getPathInfo()).thenReturn(null);
        DrillItem di = new DrillItem(new Challenge("A", Difficulty.EASY, "", ""));
        when(drillService.getDueQueue(10, "demo-user")).thenReturn(List.of(di));

        servlet.doGet(req, resp);

        verify(req).setAttribute(eq("rows"), any());
        verify(rd).forward(req, resp);
    }

    /**
     * Tests that a POST to submit code records the outcome and redirects.
     */
    @Test
    void post_submit_recordsOutcome_andRedirects() throws Exception {
        when(req.getPathInfo()).thenReturn("/42/submit");
        when(req.getParameter("language")).thenReturn("java");
        when(req.getParameter("code")).thenReturn("// code");

        when(runService.runWithMode("drill", 42L, "java", "// code")).thenReturn(new RunResult(Outcome.CORRECT, "ok"));

        servlet.doPost(req, resp);

        verify(drillService).recordOutcome(42L, Outcome.CORRECT, "// code", "demo-user");
        verify(resp).sendRedirect(contains("/drill/next"));
    }

    /**
     * Tests that a POST to add a challenge to the drill creates the DrillItem and redirects.
     */
    @Test
    void get_invalidId_returns400() throws Exception {
        when(req.getPathInfo()).thenReturn("/abc");
        servlet.doGet(req, resp);
        verify(resp).sendError(400);
    }

    /**
     * Tests that a POST to submit with an invalid ID returns 400.
     */
    @Test
    void post_submit_invalidId_returns400() throws Exception {
        when(req.getPathInfo()).thenReturn("/xyz/submit");
        servlet.doPost(req, resp);
        verify(resp).sendError(400);
    }

    /**
     * Tests that a POST to add with an invalid ID returns 400.
     */
    @Test
    void post_add_invalidId_returns400() throws Exception {
        when(req.getPathInfo()).thenReturn("/bad/add");
        servlet.doPost(req, resp);
        verify(resp).sendError(400);
    }
}
