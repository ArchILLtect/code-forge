package me.nickhanson.codeforge.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import me.nickhanson.codeforge.service.QuoteService;

/**
 * Controller to handle requests for the home page.
 * Maps "/" and "/index" to the "home" view.
 */
@Controller
public class HomeController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final QuoteService quoteService;

    /**
     * Constructor for HomeController.
     *
     * @param quoteService The QuoteService used to fetch random quotes.
     */
    public HomeController(QuoteService quoteService) { this.quoteService = quoteService; }

    /**
     * Handles GET requests to "/" and "/index".
     *
     * @return the name of the view to render (home.jsp)
     */
    @GetMapping({"/", "/index"})
    public String index(Model model, HttpSession session) {
        logger.info("Rendering home page (JSP)");
        model.addAttribute("quote", quoteService.getRandomQuote());
        Object user = session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "home"; // resolved to /WEB-INF/jsp/home.jsp via application.yml
    }
}
