package me.nickhanson.codeforge.entity;

public class AuthenticatedUser {
    private String userName;
    private String email;
    private String sub;

    public AuthenticatedUser() {}

    public AuthenticatedUser(String userName, String email, String sub) {
        this.userName = userName;
        this.email = email;
        this.sub = sub;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}