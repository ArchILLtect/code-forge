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
import me.nickhanson.codeforge.service.RunResult;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import java.util.Optional;

/**
 * Servlet handling drill-related operations, including displaying the drill queue,
 * serving challenges for drilling, and processing challenge submissions.
 * Supports URLs under /drill and /drill/*.
 *
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/drill", "/drill/*"})
public class DrillServlet extends HttpServlet {
    private boolean drillEnabled = true;
    private DrillService drillService;
    private ChallengeService challengeService;
    private ChallengeRunService runService;

    // Initialize services from servlet context or create new instances if not present.
    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        String flag = ctx.getInitParameter("features.drill.enabled");
        if (flag == null) {
            flag = ctx.getAttribute("features.drill.enabled") instanceof String
                    ? (String) ctx.getAttribute("features.drill.enabled")
                    : null;
        }
        if (flag != null) drillEnabled = Boolean.parseBoolean(flag);

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
        if (!drillEnabled) { resp.sendError(404); return; }
        String userId = UserContext.getUserId(req);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/logIn");
            return;
        }
        // /drill or /drill/*
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            // Move flash from session to request (one-time)
            var session = req.getSession(false);
            if (session != null) {
                Object flash = session.getAttribute("flashInfo");
                if (flash != null) {
                    req.setAttribute("info", flash);
                    session.removeAttribute("flashInfo");
                }
            }
            // Auto-enroll: create missing DrillItems for this user
            List<Challenge> allChallenges = challengeService.listChallenges(null);
            int created = drillService.ensureEnrollmentForUser(allChallenges, userId);
            req.setAttribute("enrolledCreated", created);
            int limit = 10;
            try {
                String raw = req.getParameter("limit");
                if (raw != null) {
                    limit = Integer.parseInt(raw);
                }
            } catch (Exception ignored) { }
            List<DrillItem> items = drillService.getDueQueue(limit, userId);
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
        // /drill/next  -> redirect to next due challenge
        String[] parts = path.substring(1).split("/");
        if (parts.length == 1 && "next".equals(parts[0])) {
            Optional<DrillItem> next = drillService.nextDue(userId);
            if (next.isPresent()) {
                resp.sendRedirect(req.getContextPath() + "/drill/" + next.get().getChallenge().getId());
            } else {
                resp.sendRedirect(req.getContextPath() + "/drill");
            }
            return;
        }
        // /drill/{id}  -> show solve page
        long id = parseIdOrBadRequest(parts[0], resp);
        if (id == -1) return;
        Challenge c = challengeService.getById(id).orElse(null);
        if (c == null) { resp.sendError(404); return; }
        DrillItem di = drillService.ensureDrillItem(id, userId);
        req.setAttribute("challenge", c);
        req.setAttribute("drillItem", di);
        req.getRequestDispatcher("/WEB-INF/jsp/drill/solve.jsp").forward(req, resp);
    }

    /**
     * Handles POST requests for submitting challenge solutions and adding challenges to the drill queue.
     * @param req the HttpServletRequest
     * @param resp the HttpServletResponse
     * @throws IOException if an I/O operation fails
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!drillEnabled) { resp.sendError(404); return; }
        String userId = UserContext.getUserId(req);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/logIn");
            return;
        }
        String path = req.getPathInfo();
        String[] parts = (path == null || "/".equals(path))
                ? new String[0]
                : path.substring(1).split("/");
        if (parts.length == 2 && "submit".equals(parts[1])) {
            long id = parseIdOrBadRequest(parts[0], resp);
            if (id == -1) return;

            String language = req.getParameter("language");
            String code = req.getParameter("code");

            RunResult result = runService.run(id, language, code);
            Outcome outcome = result.getOutcome();

            drillService.recordOutcome(id, outcome, code, userId);

            // flash message (guard for test environments where session may be null)
            javax.servlet.http.HttpSession session = req.getSession(false);
            String msg = outcome + " — " + result.getMessage();
            if (session != null) {
                session.setAttribute("flashInfo", msg);
            } else {
                req.setAttribute("info", msg);
            }

            resp.sendRedirect(req.getContextPath() + "/drill/next");
            return;
        }

        // /drill/{id}/add
        if (parts.length == 2 && "add".equals(parts[1])) {
            long id = parseIdOrBadRequest(parts[0], resp);
            if (id == -1) return;

            drillService.ensureDrillItem(id, userId);
            resp.sendRedirect(req.getContextPath() + "/challenges/" + id);
            return;
        }

        resp.sendError(400);
    }

    // ---- helpers (DRY) ----
    private long parseIdOrBadRequest(String segment, HttpServletResponse resp) throws IOException {
        try {
            long id = Long.parseLong(segment);
            if (id <= 0) {
                resp.sendError(400); return -1;
            }
            return id;
        } catch (NumberFormatException nfe) {
            resp.sendError(400);
            return -1;
        }
    }
}
