package me.nickhanson.codeforge.controller;

import me.nickhanson.codeforge.config.EnvConfig;
import me.nickhanson.codeforge.config.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

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
