package me.nickhanson.codeforge.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Simple servlet to render the current authenticated user details.
 * Redirect target after successful Cognito auth.
 *
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/me"})
public class MeServlet extends HttpServlet {

    /**
     * Handles GET requests to display the current authenticated user details.
     * @param req  the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Copy user from session to request for JSP convenience
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object user = session.getAttribute("user");
            if (user != null) {
                req.setAttribute("user", user);
            }
        }
        req.getRequestDispatcher("/WEB-INF/jsp/auth/me.jsp").forward(req, resp);
    }
}


