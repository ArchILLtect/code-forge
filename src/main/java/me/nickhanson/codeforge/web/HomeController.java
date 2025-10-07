package me.nickhanson.codeforge.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping({"/", "/index"})
    public String index() {
        log.info("Rendering home page (JSP)");
        return "home"; // resolved to /WEB-INF/jsp/home.jsp via application.yml
    }
}
