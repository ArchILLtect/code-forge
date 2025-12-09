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

@WebServlet(urlPatterns = {"/practice", "/practice/*"})
public class PracticeServlet extends HttpServlet {

    private boolean practiceEnabled = true;
    private ChallengeRunService runService;
    private me.nickhanson.codeforge.service.ChallengeService challengeService;

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
