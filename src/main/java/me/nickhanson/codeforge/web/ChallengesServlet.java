package me.nickhanson.codeforge.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.service.ChallengeService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Servlet for handling CRUD operations on Challenge entities.
 * Supports listing, creating, viewing, editing, and deleting challenges.
 * URL Patterns:
 * - GET /challenges : List all challenges (optionally filtered by difficulty)
 * - GET /challenges/new : Show form to create a new challenge
 * - POST /challenges : Create a new challenge
 * - GET /challenges/{id} : View details of a specific challenge
 * - GET /challenges/{id}/edit : Show form to edit an existing challenge
 * - POST /challenges/{id} : Update an existing challenge
 * - POST /challenges/{id}/delete : Delete a specific challenge
 *
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/challenges", "/challenges/*"})
public class ChallengesServlet extends HttpServlet {
    private ChallengeService service;

    // Initialize the servlet and obtain the ChallengeService from the servlet context
    @Override
    public void init() {
        this.service = (ChallengeService) getServletContext().getAttribute("challengeService");
        if (this.service == null) this.service = new ChallengeService();
    }

    /**
     * Handles GET requests for listing, viewing, and editing challenges.
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException the servlet exception
     * @throws IOException the IO exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        // /challenges or /challenges/
        if (path == null || "/".equals(path)) {
            // list
            Difficulty d = parseDifficultyParam(req);
            List<Challenge> list = service.listChallenges(d);
            req.setAttribute("challenges", list);
            req.setAttribute("difficulty", d);
            req.setAttribute("difficultyValue", d != null ? d.name() : "");
            req.getRequestDispatcher("/WEB-INF/jsp/challenges/list.jsp").forward(req, resp);
            return;
        }
        // /{id} or /new or /{id}/edit
        String[] parts = path.substring(1).split("/");
        if (parts.length == 1 && "new".equals(parts[0])) {
            req.setAttribute("difficulties", Difficulty.values());
            req.getRequestDispatcher("/WEB-INF/jsp/challenges/new.jsp").forward(req, resp);
            return;
        }
        // edit
        if (parts.length == 2 && "edit".equals(parts[1])) {
            Long id = parseIdOrBadRequest(parts[0], resp);
            if (id == null) return;
            Challenge c = service.getById(id).orElse(null);
            if (c == null) { resp.sendError(404); return; }
            applyEditAttributes(req, c, id);
            // Also provide the full entity for JSPs that reference ${challenge.*}
            req.setAttribute("challenge", c);
            req.getRequestDispatcher("/WEB-INF/jsp/challenges/edit.jsp").forward(req, resp);
            return;
        }
        // detail
        Long id = parseIdOrBadRequest(parts[0], resp);
        if (id == null) return;
        Optional<Challenge> opt = service.getById(id);
        if (opt.isEmpty()) { resp.sendError(404); return; }
        req.setAttribute("challenge", opt.get());
        // drillEnrolled left to DrillServlet
        req.setAttribute("drillEnrolled", Boolean.FALSE);
        req.getRequestDispatcher("/WEB-INF/jsp/challenges/detail.jsp").forward(req, resp);
    }

    /**
     * Handles POST requests for creating, updating, and deleting challenges.
     * @param req the HttpServletRequest
     * @param resp the HttpServletResponse
     * @throws IOException if an I/O error occurs while sending redirect or error
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        // /challenges or /challenges/
        if (path == null || path.equals("/")) {
            // create
            ChallengeForm form = buildFormFromRequest(req);
            Challenge saved = service.create(form);
            resp.sendRedirect(req.getContextPath() + "/challenges/" + saved.getId());
            return;
        }
        // /{id} or /{id}/delete
        String[] parts = path.substring(1).split("/");
        Long id = parseIdOrBadRequest(parts[0], resp);
        if (id == null) return;
        if (parts.length == 1) {
            // update
            ChallengeForm form = buildFormFromRequest(req);
            service.update(id, form);
            resp.sendRedirect(req.getContextPath() + "/challenges/" + id);
            return;
        }
        // delete
        if (parts.length == 2 && "delete".equals(parts[1])) {
            service.delete(id);
            resp.sendRedirect(req.getContextPath() + "/challenges");
            return;
        }
        resp.sendError(400);
    }

    // ---- helpers (DRY) ----

    private Difficulty parseDifficultyParam(HttpServletRequest req) {
        String diff = req.getParameter("difficulty");
        if (diff == null || diff.isBlank() || "All".equalsIgnoreCase(diff)) return null;
        try {
            return Difficulty.valueOf(diff);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Long parseIdOrBadRequest(String segment, HttpServletResponse resp) throws IOException {
        try {
            long id = Long.parseLong(segment);
            if (id <= 0) { resp.sendError(400); return null; }
            return id;
        } catch (NumberFormatException nfe) {
            resp.sendError(400);
            return null;
        }
    }

    private void applyEditAttributes(HttpServletRequest req, Challenge c, long id) {
        req.setAttribute("title", c.getTitle());
        req.setAttribute("difficulty", c.getDifficulty());
        req.setAttribute("blurb", c.getBlurb());
        req.setAttribute("promptMd", c.getPromptMd());
        req.setAttribute("difficulties", Difficulty.values());
        req.setAttribute("challengeId", id);
    }

    private ChallengeForm buildFormFromRequest(HttpServletRequest req) {
        ChallengeForm form = new ChallengeForm();
        form.setTitle(req.getParameter("title"));
        form.setBlurb(req.getParameter("blurb"));
        try { form.setDifficulty(Difficulty.valueOf(req.getParameter("difficulty"))); } catch (Exception ignored) {}
        form.setPromptMd(req.getParameter("promptMd"));
        form.setExpectedAnswer(req.getParameter("expectedAnswer"));
        return form;
    }
}
