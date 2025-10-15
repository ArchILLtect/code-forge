package me.nickhanson.codeforge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import me.nickhanson.codeforge.service.QuoteService;

/**
 * Controller to handle requests for the home page.
 * Maps "/" and "/index" to the "home" view.
 */
@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

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
    public String index(Model model) {
        log.info("Rendering home page (JSP)");
        model.addAttribute("quote", quoteService.getRandomQuote());
        return "home"; // resolved to /WEB-INF/jsp/home.jsp via application.yml
    }
}
