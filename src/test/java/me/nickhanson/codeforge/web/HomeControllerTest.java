package me.nickhanson.codeforge.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for the HomeController class.
 * This class uses Spring Boot's @WebMvcTest to test the controller layer in isolation.
 */
@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test configuration to define a custom InternalResourceViewResolver bean.
     * This ensures that JSP views are resolved correctly during the test.
     */
    @TestConfiguration
    static class ViewResolverConfig {
        @Bean
        InternalResourceViewResolver defaultViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix("/WEB-INF/jsp/");
            resolver.setSuffix(".jsp");
            return resolver;
        }
    }

    /**
     * Tests that the home endpoint ("/") renders the "home.jsp" view.
     * Verifies that the HTTP status is 200 (OK) and the correct view name is returned.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void home_shouldRenderHomeJsp() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}
