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
 *
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
            String diff = req.getParameter("difficulty");
            Difficulty d = null;
            if (diff != null && !diff.isBlank() && !"All".equalsIgnoreCase(diff)) {
                try { d = Difficulty.valueOf(diff); } catch (Exception ignored) {}
            }
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
            Long id = Long.valueOf(parts[0]);
            Challenge c = service.getById(id).orElse(null);
            if (c == null) { resp.sendError(404); return; }
            req.setAttribute("title", c.getTitle());
            req.setAttribute("difficulty", c.getDifficulty());
            req.setAttribute("blurb", c.getBlurb());
            req.setAttribute("promptMd", c.getPromptMd());
            req.setAttribute("difficulties", Difficulty.values());
            req.setAttribute("challengeId", id);
            req.getRequestDispatcher("/WEB-INF/jsp/challenges/edit.jsp").forward(req, resp);
            return;
        }
        // detail
        Long id = Long.valueOf(parts[0]);
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
     * @throws ServletException the servlet exception
     * @throws IOException the IO exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        // /challenges or /challenges/
        if (path == null || path.equals("/")) {
            // create
            ChallengeForm form = new ChallengeForm();
            form.setTitle(req.getParameter("title"));
            form.setBlurb(req.getParameter("blurb"));
            try { form.setDifficulty(Difficulty.valueOf(req.getParameter("difficulty"))); } catch (Exception ignored) {}
            form.setPromptMd(req.getParameter("promptMd"));
            Challenge saved = service.create(form);
            resp.sendRedirect(req.getContextPath() + "/challenges/" + saved.getId());
            return;
        }
        // /{id} or /{id}/delete
        String[] parts = path.substring(1).split("/");
        Long id = Long.valueOf(parts[0]);
        if (parts.length == 1) {
            // update
            ChallengeForm form = new ChallengeForm();
            form.setTitle(req.getParameter("title"));
            form.setBlurb(req.getParameter("blurb"));
            try { form.setDifficulty(Difficulty.valueOf(req.getParameter("difficulty"))); } catch (Exception ignored) {}
            form.setPromptMd(req.getParameter("promptMd"));
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
}
