package me.nickhanson.codeforge.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper for reading the authenticated user context (Cognito sub) from the session.
 * Centralizes logic to avoid repeated boilerplate in servlets.
 */
public final class UserContext {
    private UserContext() {}

    /**
     * Returns the Cognito subject (userId) stored in session under key "userSub".
     * @param req HttpServletRequest
     * @return userId string or null if not authenticated
     */
    public static String getUserId(HttpServletRequest req) {
        Object val = req.getSession(false) != null ? req.getSession(false).getAttribute("userSub") : null;
        return (val instanceof String s && !s.isBlank()) ? s : null;
    }

    /**
     * Convenience method to check if the request is authenticated.
     * @param req HttpServletRequest
     * @return true if a non-blank userSub is present in session
     */
    public static boolean isAuthenticated(HttpServletRequest req) {
        return getUserId(req) != null;
    }
}

