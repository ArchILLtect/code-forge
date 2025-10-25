package me.nickhanson.codeforge.auth;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Keys{

    @JsonProperty("keys")
    private List<KeysItem> keys;

    public List<KeysItem> getKeys(){
        return keys;
    }
}