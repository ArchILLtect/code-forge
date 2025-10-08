package me.nickhanson.codeforge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple health check controller that responds to /healthz requests.
 * This can be used by load balancers or monitoring tools to verify that the application is running.
 */
@RestController
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    /**
     * Handles GET requests to /healthz.
     *
     * @return a simple "ok" string indicating the application is healthy
     */
    @GetMapping("/healthz")
    public String health() {
        log.info("Health check requested");
        return "ok";
    }
}