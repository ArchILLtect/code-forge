package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.service.ChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the ChallengesController.
 * Uses MockMvc to simulate HTTP requests and verify responses.
 */
@WebMvcTest(controllers = {ChallengesController.class, ErrorControllerAdvice.class})
class ChallengesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService service;

    /**
     * Configuration to set up a view resolver for testing.
     * This allows the tests to verify view names without needing actual JSP files.
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
     * Test cases for the ChallengesController.
     * Each test simulates a specific HTTP request and verifies the response.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void list_shouldRenderListJsp() throws Exception {
        Page<Challenge> empty = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(service.listChallenges(nullable(Difficulty.class), any())).thenReturn(empty);

        mockMvc.perform(get("/challenges"))
                .andExpect(status().isOk())
                .andExpect(view().name("challenges/list"));
    }

    /**
     * Tests that the detail endpoint renders the "error/404" JSP view for an unknown challenge ID.
     * Mocks the service to return an empty result for the given ID and verifies that a 404 error view is returned.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void detail_unknownId_shouldRender404Jsp() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/challenges/99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }

    /**
     * Test the new challenge form endpoint.
     * Simulates a GET request to "/challenges/new" and verifies that the "challenges/new" JSP view is rendered.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void newForm_shouldRenderNewJsp() throws Exception {
        mockMvc.perform(get("/challenges/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("challenges/new"));
    }

    /**
     * Test creating a new challenge with valid data.
     * Mocks the service to simulate successful creation and verifies redirection to the detail view.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void create_valid_shouldRedirectToDetail() throws Exception {
        when(service.titleExists(anyString())).thenReturn(false);
        Challenge saved = new Challenge("Title", Difficulty.EASY, "blurb", "prompt");
        saved.setId(123L);
        when(service.create(any(ChallengeForm.class))).thenReturn(saved);

        mockMvc.perform(post("/challenges")
                        .param("title", "Unique Title")
                        .param("difficulty", "EASY")
                        .param("blurb", "A short summary")
                        .param("promptMd", "Prompt body"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/challenges/123"))
                .andExpect(flash().attributeExists("success"));
    }

    /**
     * Test creating a new challenge with a duplicate title.
     * Mocks the service to simulate a duplicate title scenario and verifies that the form is re-rendered with an error.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void create_duplicateTitle_shouldReturnNewWithError() throws Exception {
        when(service.titleExists(anyString())).thenReturn(true);

        mockMvc.perform(post("/challenges")
                        .param("title", "Duplicate Title")
                        .param("difficulty", "EASY")
                        .param("blurb", "A short summary")
                        .param("promptMd", "Prompt body"))
                .andExpect(status().isOk())
                .andExpect(view().name("challenges/new"));
    }
}
