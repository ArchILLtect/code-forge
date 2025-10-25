package me.nickhanson.codeforge.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

/**
 * Controller to handle requests for the "/me" endpoint, showing authenticated user details.
 * Maps "/me" to the "auth/me" view.
 */
@Controller
public class Me {

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Handles GET requests to "/me".
     */
    @GetMapping({"/me"})
    public String index(Model model, HttpSession session) {
        logger.info("Rendering me page (JSP)");
        Object user = session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "auth/me"; // resolved to /WEB-INF/jsp/auth/me.jsp via application.yml
    }
}