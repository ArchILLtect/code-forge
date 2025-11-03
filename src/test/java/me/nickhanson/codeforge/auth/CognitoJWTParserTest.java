package me.nickhanson.codeforge.auth;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CognitoJWTParserTest {

    @Test
    void validateJWT_throwsOnBadToken() {
        // CognitoJWTParser.validateJWT throws java.security.InvalidParameterException on malformed JWT
        assertThrows(java.security.InvalidParameterException.class, () -> CognitoJWTParser.validateJWT("notajwt"));
    }

    @Test
    void getPayload_and_getClaim_returnsClaimValue() {
        // create a simple jwt-like string: header.payload.signature where payload has {"sub":"123","email":"a@b"}
        String headerJson = "{\"alg\":\"none\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"123\",\"email\":\"a@b.com\"}";
        String signature = "sig";

        String token = Base64.getEncoder().encodeToString(headerJson.getBytes()) + "."
                + Base64.getEncoder().encodeToString(payloadJson.getBytes()) + "."
                + Base64.getEncoder().encodeToString(signature.getBytes());

        String sub = CognitoJWTParser.getClaim(token, "sub");
        String email = CognitoJWTParser.getClaim(token, "email");

        assertEquals("123", sub);
        assertEquals("a@b.com", email);

        // also verify header parsing
        assertEquals("none", CognitoJWTParser.getHeader(token).getString("alg"));
    }
}
