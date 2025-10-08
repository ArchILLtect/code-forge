package me.nickhanson.codeforge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle requests for the home page.
 * Maps "/" and "/index" to the "home" view.
 */
@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    /**
     * Handles GET requests to "/" and "/index".
     *
     * @return the name of the view to render (home.jsp)
     */
    @GetMapping({"/", "/index"})
    public String index() {
        log.info("Rendering home page (JSP)");
        return "home"; // resolved to /WEB-INF/jsp/home.jsp via application.yml
    }
}
