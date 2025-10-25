package me.nickhanson.codeforge.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;

    public String getAccessToken(){
        return accessToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public String getIdToken(){
        return idToken;
    }

    public String getTokenType(){
        return tokenType;
    }

    public int getExpiresIn(){
        return expiresIn;
    }
}