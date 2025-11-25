package me.nickhanson.codeforge.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.DrillItem;
import me.nickhanson.codeforge.entity.Outcome;
import me.nickhanson.codeforge.service.ChallengeRunService;
import me.nickhanson.codeforge.service.ChallengeService;
import me.nickhanson.codeforge.service.DrillService;

import java.io.IOException;
import java.util.List;

/**
 * Servlet handling drill-related operations, including displaying the drill queue,
 * serving challenges for drilling, and processing challenge submissions.
 * Supports URLs under /drill and /drill/*.
 *
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/drill", "/drill/*"})
public class DrillServlet extends HttpServlet {
    private DrillService drillService;
    private ChallengeService challengeService;
    private ChallengeRunService runService;

    // Initialize services from servlet context or create new instances if not present.
    @Override
    public void init() {
        var ctx = getServletContext();
        this.drillService = (DrillService) ctx.getAttribute("drillService");
        this.challengeService = (ChallengeService) ctx.getAttribute("challengeService");
        this.runService = (ChallengeRunService) ctx.getAttribute("runService");
        if (drillService == null) this.drillService = new DrillService();
        if (challengeService == null) this.challengeService = new ChallengeService();
        if (runService == null) this.runService = new ChallengeRunService();
    }

    /**
     * Handles GET requests for the drill queue and individual challenges.
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException the servlet exception
     * @throws IOException the IO exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /drill or /drill/*
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            // queue
            int limit = 10;
            try { limit = Integer.parseInt(req.getParameter("limit")); } catch (Exception ignored) {}
            List<DrillItem> items = drillService.getDueQueue(limit);
            req.setAttribute("rows", items.stream().map(di -> new DrillQueueRow(
                    di.getChallenge().getId(),
                    di.getChallenge().getTitle(),
                    di.getChallenge().getDifficulty(),
                    di.getNextDueAt()
            )).toList());
            req.setAttribute("count", items.size());
            req.getRequestDispatcher("/WEB-INF/jsp/drill/queue.jsp").forward(req, resp);
            return;
        }
        // /next
        String[] parts = path.substring(1).split("/");
        if (parts.length == 1 && "next".equals(parts[0])) {
            var next = drillService.nextDue();
            if (next.isPresent()) {
                resp.sendRedirect(req.getContextPath() + "/drill/" + next.get().getChallenge().getId());
            } else {
                resp.sendRedirect(req.getContextPath() + "/drill");
            }
            return;
        }
        // /{id}
        long id;
        try {
            id = Long.parseLong(parts[0]);
            if (id <= 0) { resp.sendError(400); return; }
        } catch (NumberFormatException nfe) {
            resp.sendError(400);
            return;
        }
        Challenge c = challengeService.getById(id).orElse(null);
        if (c == null) { resp.sendError(404); return; }
        DrillItem di = drillService.ensureDrillItem(id);
        req.setAttribute("challenge", c);
        req.setAttribute("drillItem", di);
        req.getRequestDispatcher("/WEB-INF/jsp/drill/solve.jsp").forward(req, resp);
    }

    /**
     * Handles POST requests for submitting challenge solutions and adding challenges to the drill queue.
     * @param req the HttpServletRequest
     * @param resp the HttpServletResponse
     * @throws ServletException the servlet exception
     * @throws IOException the IO exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /{id}/submit
        String path = req.getPathInfo();
        String[] parts = (path == null || "/".equals(path)) ? new String[0] : path.substring(1).split("/");
        if (parts.length == 2 && "submit".equals(parts[1])) {
            long id;
            try {
                id = Long.parseLong(parts[0]);
                if (id <= 0) { resp.sendError(400); return; }
            } catch (NumberFormatException nfe) {
                resp.sendError(400); return; }
            String language = req.getParameter("language");
            String code = req.getParameter("code");
            var result = runService.run(id, language, code);
            Outcome outcome = result.getOutcome();
            drillService.recordOutcome(id, outcome, code);
            resp.sendRedirect(req.getContextPath() + "/drill/next");
            return;
        }
        // /{id}/add
        if (parts.length == 2 && "add".equals(parts[1])) {
            long id;
            try {
                id = Long.parseLong(parts[0]);
                if (id <= 0) { resp.sendError(400); return; }
            } catch (NumberFormatException nfe) { resp.sendError(400); return; }
            drillService.ensureDrillItem(id);
            resp.sendRedirect(req.getContextPath() + "/challenges/" + id);
            return;
        }
        resp.sendError(400);
    }
}
