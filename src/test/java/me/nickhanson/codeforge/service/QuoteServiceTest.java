package me.nickhanson.codeforge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.nickhanson.codeforge.entity.QuoteResponseItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteServiceTest {

    @Test
    void apiMapping_parsesExpectedFields() throws Exception {
        String json = "[{\"q\":\"Hello\",\"a\":\"World\",\"h\":\"<blockquote/>\"}]";
        ObjectMapper mapper = new ObjectMapper();

        // Act: map JSON → POJO array
        QuoteResponseItem[] arr = mapper.readValue(json, QuoteResponseItem[].class);

        // Assert: verify that Jackson mapped the fields correctly
        assertNotNull(arr);
        assertEquals(1, arr.length);
        assertEquals("Hello", arr[0].getQ());
        assertEquals("World", arr[0].getA());
        assertEquals("<blockquote/>", arr[0].getH());
    }
}
