package me.nickhanson.codeforge.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class AuthGuardFilterTest {

    private final AuthGuardFilter filter = new AuthGuardFilter();

    @Test
    void unauthenticated_get_new_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/challenges/new");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void unauthenticated_post_create_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/challenges");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void public_get_list_passes_through() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/challenges");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        TrackingFilterChain chain = new TrackingFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isNull();
        assertThat(chain.called).isTrue();
    }

    @Test
    void unauthenticated_get_edit_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/challenges/123/edit");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void unauthenticated_post_delete_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/challenges/123/delete");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    // New coverage: updating a challenge requires auth
    @Test
    void unauthenticated_post_update_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/challenges/123");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    // Happy paths: authenticated user should pass through protected endpoints
    @Test
    void authenticated_get_new_allows_through() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/challenges/new");
        req.getSession(true).setAttribute("user", "tester");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        TrackingFilterChain chain = new TrackingFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isNull();
        assertThat(chain.called).isTrue();
    }

    @Test
    void authenticated_post_update_allows_through() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/challenges/123");
        req.getSession(true).setAttribute("user", "tester");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        TrackingFilterChain chain = new TrackingFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isNull();
        assertThat(chain.called).isTrue();
    }

    @Test
    void unauthenticated_get_drill_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/drill");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void unauthenticated_get_drill_next_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/drill/next");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void unauthenticated_get_drill_solve_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/drill/42");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void unauthenticated_post_drill_submit_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/drill/42/submit");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    @Test
    void authenticated_get_drill_allows_through() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/drill");
        req.getSession(true).setAttribute("user", "tester");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        TrackingFilterChain chain = new TrackingFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isNull();
        assertThat(chain.called).isTrue();
    }

    @Test
    void authenticated_post_drill_submit_allows_through() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/drill/42/submit");
        req.getSession(true).setAttribute("user", "tester");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        TrackingFilterChain chain = new TrackingFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isNull();
        assertThat(chain.called).isTrue();
    }

    // Bonus: add is also protected for POST
    @Test
    void unauthenticated_post_drill_add_redirects_to_login() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/drill/42/add");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(req, resp, chain);

        assertThat(resp.getRedirectedUrl()).isEqualTo("/logIn");
    }

    static class TrackingFilterChain extends MockFilterChain {
        boolean called = false;
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) {
            called = true;
        }
    }
}
