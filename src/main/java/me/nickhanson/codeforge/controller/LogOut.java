package me.nickhanson.codeforge.controller;

import me.nickhanson.codeforge.config.EnvConfig;
import me.nickhanson.codeforge.config.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Handles user logout by invalidating the local session and redirecting to Cognito logout.
 * Sends the user to the Cognito logout endpoint, which then redirects back to the application.
 * @author Nick Hanson
 */
@WebServlet("/logout")
public class LogOut extends HttpServlet implements PropertiesLoader {
    private String LOGOUT_URL;
    private String LOGOUT_REDIRECT;
    private String CLIENT_ID;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void init() {
        Properties properties = loadProperties("/cognito.properties");

        LOGOUT_URL = properties.getProperty("logoutURL");
        LOGOUT_REDIRECT = EnvConfig.get(logger, properties, "logoutRedirectURL");
        CLIENT_ID = properties.getProperty("client.id");
    }

    /**
     * Handles logout by invalidating the local session and redirecting to Cognito logout.
     * @param req the HTTP request
     * @param resp the HTTP response
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // kill local session
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // send user to Cognito logout, then back to our app
        String url = LOGOUT_URL
                + "?client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8)
                + "&logout_uri=" + URLEncoder.encode(LOGOUT_REDIRECT, StandardCharsets.UTF_8);
        resp.sendRedirect(url);
    }
}
