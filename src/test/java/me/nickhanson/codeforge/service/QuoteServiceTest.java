package me.nickhanson.codeforge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.nickhanson.codeforge.entity.QuoteResponseItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock HttpClient http;
    @Mock HttpResponse<String> httpResponse;

    private QuoteService service;

    @BeforeEach
    void setup() throws Exception {
        service = new QuoteService();
        // Inject mocked HttpClient into the service (avoid real network)
        setPrivateField(service, "http", http);
        // Reduce cache duration if needed in future (defaults are fine here)
    }

    /**
     * Test that the JSON mapping from the quote API to QuoteResponseItem works as expected.
     * @throws Exception if mapping fails
     */
    @Test
    void apiMapping_parsesExpectedFields() throws Exception {
        String json = "[{\"_id\":\"A5l8yCGO4BL5\",\"content\":\"Forgiveness is choosing to love. It is the first skill of self-giving love.\",\"author\":\"Mahatma Gandhi\",\"tags\":[\"Wisdom\"],\"authorSlug\":\"mahatma-gandhi\",\"length\":75,\"dateAdded\":\"2019-12-13\",\"dateModified\":\"2023-04-14\"}]";
        ObjectMapper mapper = new ObjectMapper();

        // Act: map JSON → POJO array
        QuoteResponseItem[] arr = mapper.readValue(json, QuoteResponseItem[].class);

        // Assert: verify that Jackson mapped the fields correctly
        assertNotNull(arr);
        assertEquals(1, arr.length);
        assertEquals("Forgiveness is choosing to love. It is the first skill of self-giving love.", arr[0].getContent());
        assertEquals("Mahatma Gandhi", arr[0].getAuthor());
        assertEquals(75, arr[0].getLength());
    }

    /**
     * Test that getRandomQuote correctly formats a single-element response and caches it.
     * @throws Exception if HTTP call fails
     */
    @Test
    void getRandomQuote_formatsSingleElementResponse_andCaches() throws Exception {
        String body = "[{\"content\":\"Hello\",\"author\":\"World\"}]";
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(body);
        when(http.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponse);

        String first = service.getRandomQuote();
        assertEquals("\u201cHello\u201d — World", first);

        // Second call should use cache; http.send still only 1 call
        String second = service.getRandomQuote();
        assertEquals(first, second);
        verify(http, times(1)).send(any(HttpRequest.class), any());
    }

    /**
     * Test that getRandomQuote falls back to default quote on non-200 response.
     * @throws Exception if HTTP call fails
     */
    @Test
    void getRandomQuote_fallbackOnNon200() throws Exception {
        when(httpResponse.statusCode()).thenReturn(500);
        when(httpResponse.body()).thenReturn("oops");
        when(http.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponse);

        String out = service.getRandomQuote();
        assertEquals("Keep it simple. — CodeForge", out);
    }

    /**
     * Test that getRandomQuote falls back to default quote on empty array response.
     * @throws Exception if HTTP call fails
     */
    @Test
    void getRandomQuote_fallbackOnEmptyArray() throws Exception {
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("[]");
        when(http.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponse);

        String out = service.getRandomQuote();
        assertEquals("Keep it simple. — CodeForge", out);
    }

    /**
     * Test that getRandomQuote returns one of multiple quotes in the response array.
     * @throws Exception if HTTP call fails
     */
    @Test
    void getRandomQuote_multiElementArray_returnsOneOfThem() throws Exception {
        // Disable cache so we can safely reason about output per call if needed
        setPrivateField(service, "cacheDuration", 0L);

        String body = "[" +
                "{\"content\":\"Alpha\",\"author\":\"A\"}," +
                "{\"content\":\"Beta\",\"author\":\"B\"}" +
                "]";
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(body);
        when(http.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponse);

        String out = service.getRandomQuote();
        assertTrue(out.equals("\u201cAlpha\u201d — A") || out.equals("\u201cBeta\u201d — B"));
    }

    /**
     * Test that getRandomQuote defaults to "Unknown" author when the author field is blank.
     * @throws Exception if HTTP call fails
     */
    @Test
    void getRandomQuote_blankAuthor_defaultsToUnknown() throws Exception {
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("[{\"content\":\"Msg\",\"author\":\"\"}]");
        when(http.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponse);

        String out = service.getRandomQuote();
        assertEquals("\u201cMsg\u201d — Unknown", out);
    }

    // --- helpers ---

    /**
     * Utility to set private fields via reflection for testing.
     * @param target the object whose field to set
     * @param fieldName the name of the field
     * @param value the value to set
     * @throws Exception if reflection fails
     */
    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
