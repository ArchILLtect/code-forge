package me.nickhanson.codeforge.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Extract standard servlet error attributes
        Integer status = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable throwable = (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String requestUri = (String) req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String message = (String) req.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (status == null) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        if (requestUri == null || requestUri.isEmpty()) {
            requestUri = req.getRequestURI();
        }

        // Provide a friendly, concise errorMessage for the JSP (500.jsp expects this optionally)
        String errorMessage;
        if (message != null && !message.isBlank()) {
            errorMessage = message;
        } else if (throwable != null) {
            errorMessage = Objects.toString(throwable.getMessage(), throwable.getClass().getSimpleName());
        } else {
            errorMessage = "Unexpected error";
        }

        // Common extra context useful in views or logs
        req.setAttribute("timestamp", Instant.now().toString());
        req.setAttribute("referer", req.getHeader("Referer"));

        // Align with existing JSPs: they read errorMessage and javax.servlet.error.request_uri
        req.setAttribute("errorMessage", errorMessage);
        req.setAttribute("javax.servlet.error.request_uri", requestUri);
        req.setAttribute("javax.servlet.error.status_code", status);

        // Choose view based on status code
        String view;
        switch (status) {
            case HttpServletResponse.SC_FORBIDDEN: // 403
                view = "/WEB-INF/jsp/error/403.jsp";
                break;
            case HttpServletResponse.SC_NOT_FOUND: // 404
                view = "/WEB-INF/jsp/error/404.jsp";
                break;
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR: // 500
            case HttpServletResponse.SC_BAD_REQUEST: // 400 -> treat as 500 view for now
            default:
                view = "/WEB-INF/jsp/error/500.jsp";
                break;
        }

        // Set the response status code
        resp.setStatus(status);

        // Forward to the chosen error page
        req.getRequestDispatcher(view).forward(req, resp);
    }
}
