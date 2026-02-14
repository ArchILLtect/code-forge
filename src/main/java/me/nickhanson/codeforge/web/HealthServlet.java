package me.nickhanson.codeforge.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Lightweight readiness/liveness endpoint.
 * Returns 200 quickly without touching downstream dependencies.
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/health"})
public class HealthServlet extends HttpServlet {

    /**
     * Handles health-check requests by returning an immediate {@code 200 OK}
     * response with a plain-text {@code OK} body.
     *
     * @param req  the incoming HTTP request
     * @param resp the HTTP response used to return health status
     * @throws IOException if writing the response fails
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("OK");
    }
}
