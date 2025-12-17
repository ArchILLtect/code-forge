package me.nickhanson.codeforge.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseMappingTest {

    /**
     * Verifies that Jackson correctly maps a JSON string to a TokenResponse object.
     * @throws Exception if mapping fails
     */
    @Test
    void jackson_maps_json_to_tokenResponse() throws Exception {
        String json = "{\"access_token\":\"a\",\"refresh_token\":\"r\",\"id_token\":\"i\",\"token_type\":\"t\",\"expires_in\":3600}";
        ObjectMapper m = new ObjectMapper();
        TokenResponse tr = m.readValue(json, TokenResponse.class);

        assertEquals("a", tr.getAccessToken());
        assertEquals("r", tr.getRefreshToken());
        assertEquals("i", tr.getIdToken());
        assertEquals("t", tr.getTokenType());
        assertEquals(3600, tr.getExpiresIn());
    }
}

