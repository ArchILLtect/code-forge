package me.nickhanson.codeforge.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(contextPath.length());
        String method = req.getMethod();

        boolean needsAuth = false;

        // 1) Always allow static assets
        if (path.startsWith("/css/")
                || path.startsWith("/images/")
                || path.startsWith("/assets/")
                || path.startsWith("/static/")
                || path.startsWith("/apidocs/")
                || path.equals("/favicon.ico")
                || path.startsWith("/favicon")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".gif")
                || path.endsWith(".svg")
                || path.endsWith(".webp")) {
            chain.doFilter(req, resp);
            return;
        }

// 2) Allow your public pages (home/about/login/error)
        if (path.equals("/") || path.equals("/home")
                || path.startsWith("/about")
                || path.equals("/health")
                || path.startsWith("/logIn")
                || path.startsWith("/logout")
                || path.startsWith("/error")) {
            chain.doFilter(req, resp);
            return;
        }

        // Check if the request method is POST.
        if ("POST".equalsIgnoreCase(method)) {
            // Public practice submissions do NOT require auth
            if (path.matches("^/practice/\\d+/submit$")) {
                needsAuth = false;
            }
            // Require authentication for creating a challenge.
            else if ("/challenges".equals(path)) {
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
            // Practice pages are public
            if (path.equals("/practice") || path.matches("^/practice(/.*)?$")) {
                needsAuth = false;
            }
            // Require authentication for specific GET paths.
            // Show forms that should be restricted to authenticated users
            else if (PROTECTED_GET_PATHS.contains(path) || path.matches("^/challenges/\\d+/edit$")) {
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
            // Use unified user context; accept either userSub or legacy 'user'
            boolean authed = UserContext.isAuthenticated(req);
            if (!authed) {
                var session = req.getSession(false);
                Object legacy = (session != null) ? session.getAttribute("user") : null;
                authed = (legacy != null);
            }
            if (!authed) {
                resp.sendRedirect(contextPath + "/logIn");
                return;
            }
        }

        // Continue the filter chain if authentication is not required or the user is authenticated.
        chain.doFilter(req, resp);
    }
}

