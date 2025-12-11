package me.nickhanson.codeforge.web;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * A servlet filter that sets the character encoding for requests and responses to UTF-8.
 * This ensures that all incoming and outgoing data is properly encoded in UTF-8.
 * Had to add this filter because Tomcat was not correctly setting UTF-8 encoding on POST requests and I wanted
 * to be able to use UTF-8 characters in form submissions--specifically emojis. lol
 *
 * @author Nick Hanson
 */
@WebFilter("/*")
public class Utf8EncodingFilter implements Filter {

    /**
     * Sets the character encoding for the request and response to UTF-8.
     *
     * @param request  the ServletRequest object
     * @param response the ServletResponse object
     * @param chain    the FilterChain object
     * @throws IOException      if an I/O error occurs during the filtering process
     * @throws ServletException if a servlet error occurs during the filtering process
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }
}