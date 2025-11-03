package me.nickhanson.codeforge.web;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * AuthGuardFilter is a servlet filter that enforces authentication for specific routes.
 * It intercepts HTTP requests and checks if the user is logged in before allowing access
 * to protected resources. If the user is not authenticated, they are redirected to the login page.
 */
@WebFilter(urlPatterns = "/*")
public class AuthGuardFilter extends HttpFilter {

    // A set of GET paths that require authentication.
    private static final Set<String> PROTECTED_GET_PATHS = Set.of(
            "/challenges/new"
    );

    /**
     * Filters incoming HTTP requests to enforce authentication for protected routes.
     *
     * @param req   The HttpServletRequest object that contains the request the client made.
     * @param resp  The HttpServletResponse object that contains the response the server sends.
     * @param chain The FilterChain object to pass the request and response to the next filter.
     * @throws IOException      If an input or output error occurs while processing the request.
     * @throws ServletException If the request cannot be handled.
     */
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        // Get the context path and the requested URI.
        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());
        String method = req.getMethod();

        boolean needsAuth = false;

        // Check if the request method is POST.
        if ("POST".equalsIgnoreCase(method)) {
            // Require authentication for creating a challenge.
            if ("/challenges".equals(path)) {
                needsAuth = true;
            }
            // Require authentication for updating or deleting a challenge.
            // Updating or deleting a challenge: /challenges/{id} and /challenges/{id}/delete
            else if (path.matches("^/challenges/\\d+(/delete)?$")) {
                needsAuth = true;
            }
            // Require authentication for Drill submissions and additions.
            // Drill submissions and adds: /drill/{id}/submit or /drill/{id}/add
            else if (path.matches("^/drill/\\d+/(submit|add)$")) {
                needsAuth = true;
            }
        }
        // Check if the request method is GET.
        else if ("GET".equalsIgnoreCase(method)) {
            // Require authentication for specific GET paths.
            // Show forms that should be restricted to authenticated users
            if (PROTECTED_GET_PATHS.contains(path) || path.matches("^/challenges/\\d+/edit$")) {
                needsAuth = true;
            }
            // Require authentication for all Drill pages.
            // All Drill pages require auth (queue, next, solve)
            else if ("/drill".equals(path) || path.matches("^/drill(/.*)?$")) {
                needsAuth = true;
            }
        }

        // If authentication is required, check if the user is logged in.
        if (needsAuth) {
            HttpSession session = req.getSession(false);
            Object user = (session != null) ? session.getAttribute("user") : null;
            // Redirect to the login page if the user is not authenticated.
            if (user == null) {
                resp.sendRedirect(contextPath + "/logIn");
                return;
            }
        }

        // Continue the filter chain if authentication is not required or the user is authenticated.
        chain.doFilter(req, resp);
    }
}
