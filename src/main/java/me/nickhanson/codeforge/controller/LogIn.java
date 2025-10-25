package me.nickhanson.codeforge.controller;

import me.nickhanson.codeforge.config.EnvConfig;
import me.nickhanson.codeforge.config.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebServlet(urlPatterns = {"/logIn"})

// Servlet to route to the AWS Cognito-hosted login page.
public class LogIn extends HttpServlet implements PropertiesLoader {

    public static String CLIENT_ID;
    public static String LOGIN_URL;
    public static String REDIRECT_URL;

    private final Logger logger = LogManager.getLogger(this.getClass());
    Properties properties;

    @Override
    public void init() throws ServletException {
        super.init();
        properties = loadProperties("/cognito.properties");

        CLIENT_ID = properties.getProperty("client.id");
        LOGIN_URL = properties.getProperty("loginURL");
        REDIRECT_URL = EnvConfig.get(logger, properties, "redirectURL");
    }

    /**
     * Route to the aws-hosted cognito login page.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException - if a servlet-specific error occurs
     * @throws IOException - if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("Cognito Login Redirect URL base: {}", LOGIN_URL);
        if (properties == null
                || CLIENT_ID == null
                || LOGIN_URL == null
                || REDIRECT_URL == null) {

            getServletContext().log("Cognito configuration not loaded properly — routing to error page.");
            req.setAttribute("errorMessage", "Authentication service is currently unavailable. Please try again later.");
            req.getRequestDispatcher("/WEB-INF/jsp/error/500.jsp").forward(req, resp);
            return;
        }

        // Required for Cognito authorization code flow
        String scope = "email openid phone";

        String url = String.format(
                "%s?client_id=%s&response_type=code&scope=%s&redirect_uri=%s",
                LOGIN_URL,
                URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8),
                URLEncoder.encode(scope, StandardCharsets.UTF_8),
                URLEncoder.encode(REDIRECT_URL, StandardCharsets.UTF_8)
        );

        logger.debug("Redirecting to Cognito Hosted UI: {}", url);
        resp.sendRedirect(url);
    }
}