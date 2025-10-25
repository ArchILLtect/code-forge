package me.nickhanson.codeforge.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeysItem{

    @JsonProperty("kty")
    private String kty;

    @JsonProperty("e")
    private String E;

    @JsonProperty("use")
    private String use;

    @JsonProperty("kid")
    private String kid;

    @JsonProperty("alg")
    private String alg;

    @JsonProperty("n")
    private String N;

    public String getKty(){
        return kty;
    }

    public String getE(){
        return E;
    }

    public String getUse(){
        return use;
    }

    public String getKid(){
        return kid;
    }

    public String getAlg(){
        return alg;
    }

    public String getN(){
        return N;
    }
}