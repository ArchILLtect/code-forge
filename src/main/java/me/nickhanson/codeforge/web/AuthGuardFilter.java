package me.nickhanson.codeforge.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebFilter(urlPatterns = "/*")
public class AuthGuardFilter extends HttpFilter {

    private static final Set<String> PROTECTED_GET_PATHS = Set.of(
            "/challenges/new"
    );

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());
        String method = req.getMethod();

        boolean needsAuth = false;

        if ("POST".equalsIgnoreCase(method)) {
            // Creating a challenge
            if ("/challenges".equals(path)) {
                needsAuth = true;
            }
            // Updating or deleting a challenge: /challenges/{id} and /challenges/{id}/delete
            else if (path.matches("^/challenges/\\d+$") || path.matches("^/challenges/\\d+/delete$")) {
                needsAuth = true;
            }
        } else if ("GET".equalsIgnoreCase(method)) {
            // Show forms that should be restricted to authenticated users
            if (PROTECTED_GET_PATHS.contains(path) || path.matches("^/challenges/\\d+/edit$")) {
                needsAuth = true;
            }
        }

        if (needsAuth) {
            HttpSession session = req.getSession(false);
            Object user = (session != null) ? session.getAttribute("user") : null;
            if (user == null) {
                resp.sendRedirect(contextPath + "/logIn");
                return;
            }
        }

        chain.doFilter(req, resp);
    }
}

