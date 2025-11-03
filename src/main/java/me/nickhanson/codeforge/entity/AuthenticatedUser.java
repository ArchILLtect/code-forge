package me.nickhanson.codeforge.entity;

/**
 * Represents an authenticated user in the system.
 * Contains basic user information such as username, email, and unique identifier (sub).
 * @author Nick Hanson
 *  TODO: needs Lombok implementation
 */
public class AuthenticatedUser {
    private String userName;
    private String email;
    private String sub;

    // Default constructor
    public AuthenticatedUser() {}

    // Parameterized constructor
    public AuthenticatedUser(String userName, String email, String sub) {
        this.userName = userName;
        this.email = email;
        this.sub = sub;
    }

    /**
     * Gets the username of the authenticated user.
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the authenticated user.
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the email of the authenticated user.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the authenticated user.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the unique identifier (sub) of the authenticated user.
     * @return the sub
     */
    public String getSub() {
        return sub;
    }

    /**
     * Sets the unique identifier (sub) of the authenticated user.
     * @param sub the sub to set
     */
    public void setSub(String sub) {
        this.sub = sub;
    }
}