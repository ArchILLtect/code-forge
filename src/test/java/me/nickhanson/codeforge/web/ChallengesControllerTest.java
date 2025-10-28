package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.service.ChallengeService;
import me.nickhanson.codeforge.service.DrillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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
    @MockBean
    private DrillService drillService;

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
        when(service.listChallenges(nullable(Difficulty.class)))
                .thenReturn(Collections.emptyList());

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
        mockMvc.perform(get("/challenges/new").sessionAttr("user", "test"))
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
                        .sessionAttr("user", "test")
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
                        .sessionAttr("user", "test")
                        .param("title", "Duplicate Title")
                        .param("difficulty", "EASY")
                        .param("blurb", "A short summary")
                        .param("promptMd", "Prompt body"))
                .andExpect(status().isOk())
                .andExpect(view().name("challenges/new"));
    }

    /**
     * Test updating an existing challenge with valid data.
     * Mocks the service to simulate successful update and verifies redirection to the detail view.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void update_valid_shouldRedirectToDetail() throws Exception {
        long id = 42L;
        when(service.titleExistsForOther(anyString(), eq(id))).thenReturn(false);
        Challenge updated = new Challenge("New Title", Difficulty.MEDIUM, "blurb", "prompt");
        updated.setId(id);
        when(service.update(eq(id), any(ChallengeForm.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(post("/challenges/" + id)
                        .sessionAttr("user", "test")
                        .param("title", "New Title")
                        .param("difficulty", "MEDIUM")
                        .param("blurb", "A short summary")
                        .param("promptMd", "Prompt body"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/challenges/" + id))
                .andExpect(flash().attributeExists("success"));
    }

    /**
     * Test updating a challenge with a duplicate title.
     * Mocks the service to simulate a duplicate title scenario and verifies that the edit form is re-rendered with an error.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void update_duplicateTitle_shouldReturnEditWithError() throws Exception {
        long id = 42L;
        when(service.titleExistsForOther(anyString(), eq(id))).thenReturn(true);

        mockMvc.perform(post("/challenges/" + id)
                        .sessionAttr("user", "test")
                        .param("title", "Duplicate Title")
                        .param("difficulty", "EASY")
                        .param("blurb", "A short summary")
                        .param("promptMd", "Prompt body"))
                .andExpect(status().isOk())
                .andExpect(view().name("challenges/edit"));
    }

    /**
     * Test deleting a challenge successfully.
     * Mocks the service to simulate successful deletion and verifies redirection to the challenges list with a success message.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void delete_success_shouldRedirectWithFlash() throws Exception {
        long id = 55L;
        when(service.delete(id)).thenReturn(true);

        mockMvc.perform(post("/challenges/" + id + "/delete").sessionAttr("user", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/challenges"))
                .andExpect(flash().attributeExists("success"));
    }

    /**
     * Test deleting a challenge that does not exist.
     * Mocks the service to simulate a not-found scenario and verifies redirection to the challenges list with an error message.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void delete_notFound_shouldRedirectWithErrorFlash() throws Exception {
        long id = 56L;
        when(service.delete(id)).thenReturn(false);

        mockMvc.perform(post("/challenges/" + id + "/delete").sessionAttr("user", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/challenges"))
                .andExpect(flash().attributeExists("error"));
    }

    /**
     * Test the detail endpoint for a challenge with a server-side exception.
     * Simulates the service throwing an exception and verifies that the error controller advice maps it to the "error/500" view.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void detail_serviceThrows_shouldRender500Jsp() throws Exception {
        when(service.getById(1L)).thenThrow(new RuntimeException("boom"));
        mockMvc.perform(get("/challenges/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"));
    }
}
