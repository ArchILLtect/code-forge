package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.service.QuoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet handling requests to the home page.
 * Fetches a random programming quote to display.
 *
 * @author Nick Hanson
 */
@WebServlet(urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {
    private final QuoteService quotes = new QuoteService();

    /**
     * Handles GET requests to the home page.
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @throws ServletException the servlet exception
     * @throws IOException the IO exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Fetch quote via JSON→POJO mapping; failures fall back to a static quote
        String quote = quotes.getRandomQuote();
        req.setAttribute("quote", quote);
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}