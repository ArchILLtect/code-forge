package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.service.ChallengeRunService;
import me.nickhanson.codeforge.service.RunResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet handling practice mode operations, including displaying practice challenges
 * and processing challenge submissions.
 * Supports URLs under /practice and /practice/*.
 *
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/practice", "/practice/*"})
public class PracticeServlet extends HttpServlet {

    private boolean practiceEnabled = true;
    private ChallengeRunService runService;
    private me.nickhanson.codeforge.service.ChallengeService challengeService;

    /**
     * Initializes services from servlet context or creates new instances if not present.
     */
    @Override
    public void init() {
        var ctx = getServletContext();
        String flag = ctx.getInitParameter("features.practice.enabled");
        if (flag == null) {
            flag = ctx.getAttribute("features.practice.enabled") instanceof String
                    ? (String) ctx.getAttribute("features.practice.enabled")
                    : null;
        }
        if (flag != null) {
            practiceEnabled = Boolean.parseBoolean(flag);
        }
        this.runService = (ChallengeRunService) ctx.getAttribute("runService");
        if (runService == null) this.runService = new ChallengeRunService();
        this.challengeService = (me.nickhanson.codeforge.service.ChallengeService) ctx.getAttribute("challengeService");
        if (challengeService == null) this.challengeService = new me.nickhanson.codeforge.service.ChallengeService();
    }

    /**
     * Handles GET requests for displaying practice challenges.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!practiceEnabled) { resp.sendError(404); return; }
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/jsp/practice/practice.jsp").forward(req, resp);
            return;
        }
        String[] parts = path.substring(1).split("/");
        long id = parseIdOrBadRequest(parts[0], resp);
        if (id == -1) return;
        Optional<Challenge> oc = challengeService.getById(id);
        if (oc.isEmpty()) { resp.sendError(404); return; }
        req.setAttribute("challenge", oc.get());
        req.setAttribute("mode", "practice");
        req.getRequestDispatcher("/WEB-INF/jsp/practice/solve.jsp").forward(req, resp);
    }

    /**
     * Handles POST requests for submitting practice challenge solutions.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (!practiceEnabled) { resp.sendError(404); return; }
        String path = req.getPathInfo();
        String[] parts = (path == null || "/".equals(path)) ? new String[0] : path.substring(1).split("/");
        if (parts.length == 2 && "submit".equals(parts[1])) {
            long id = parseIdOrBadRequest(parts[0], resp);
            if (id == -1) return;
            String language = req.getParameter("language");
            String code = req.getParameter("code");
            if (language == null || language.isBlank() || code == null || code.isBlank()) {
                Challenge challenge = challengeService.getById(id).orElse(null);
                if (challenge == null) { resp.sendError(404); return; }
                req.setAttribute("challenge", challenge);
                req.setAttribute("mode", "practice");
                req.setAttribute("submittedCode", code);
                req.setAttribute("outcome", me.nickhanson.codeforge.entity.Outcome.SKIPPED);
                req.setAttribute("feedback", "Missing language or code. Please fill in both fields.");
                req.getRequestDispatcher("/WEB-INF/jsp/practice/solve.jsp").forward(req, resp);
                return;
            }
            RunResult result = runService.runWithMode("practice", id, language, code);
            // Inline render: no persistence
            Challenge challenge = challengeService.getById(id).orElse(null);
            if (challenge == null) { resp.sendError(404); return; }
            req.setAttribute("challenge", challenge);
            req.setAttribute("mode", "practice");
            req.setAttribute("submittedCode", code);
            req.setAttribute("outcome", result.getOutcome());
            req.setAttribute("feedback", result.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/practice/solve.jsp").forward(req, resp);
            return;
        }
        resp.sendError(400);
    }

    /**
     * Parses a string segment as a long ID. Sends a 400 Bad Request response if parsing fails.
     *
     * @param segment the string segment to parse
     * @param resp    the HttpServletResponse to send error responses
     * @return the parsed long ID, or -1 if parsing failed
     * @throws IOException if an I/O error occurs while sending the error response
     */
    private long parseIdOrBadRequest(String segment, HttpServletResponse resp) throws IOException {
        try {
            long id = Long.parseLong(segment);
            if (id <= 0) { resp.sendError(400); return -1; }
            return id;
        } catch (NumberFormatException nfe) {
            resp.sendError(400);
            return -1;
        }
    }
}
