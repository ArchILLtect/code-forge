package me.nickhanson.codeforge.web;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for the application.
 * Provides centralized handling of exceptions and maps them to appropriate error views.
 */
@ControllerAdvice
public class ErrorControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    /**
     * Handles ResponseStatusException and maps it to an appropriate error view.
     *
     * @param ex       The ResponseStatusException to handle.
     * @param model    The model to populate with error details for the view.
     * @param response The HttpServletResponse to set the HTTP status code.
     * @return The name of the error view template.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public String handleStatus(ResponseStatusException ex, Model model, HttpServletResponse response) {
        int code = ex.getStatusCode().value();
        HttpStatus status = HttpStatus.resolve(code);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        response.setStatus(status.value());
        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("message", ex.getReason());
        if (status == HttpStatus.NOT_FOUND) {
            log.warn("404 Not Found: {}", ex.getReason());
            return "error/404";
        }
        log.error("HTTP {} {}: {}", status.value(), status.getReasonPhrase(), ex.getReason());
        return "error/500";
    }

    /**
     * Handles generic exceptions and maps them to the 500 error view.
     *
     * @param ex       The generic exception to handle.
     * @param model    The model to populate with error details for the view.
     * @param response The HttpServletResponse to set the HTTP status code.
     * @return The name of the 500 error view template.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model, HttpServletResponse response) {
        log.error("Unhandled exception", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", ex.getMessage());
        return "error/500";
    }

    /**
     * Handles NoResourceFoundException and ignores it to prevent unnecessary error handling.
     *
     * @param ex The NoResourceFoundException to handle.
     * @return A ResponseEntity with a 404 status code and no body.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> ignoreNoResourceFound(NoResourceFoundException ex) {
        // This exception is thrown by Spring when a static resource is not found.
        // We can safely ignore it to prevent unnecessary error handling.

        // return a normal 404 without a stacktrace
        return ResponseEntity.notFound().build();
    }
}
