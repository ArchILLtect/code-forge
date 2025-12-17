package me.nickhanson.codeforge.entity;

import lombok.*;

/**
 * Represents an authenticated user in the system.
 * <p>
 * Session-scoped authenticated user derived from Cognito JWT claims.
 * This class is not a persisted entity and is not managed by Hibernate.
 *
 * @author Nick Hanson
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticatedUser {
    private String userName;
    private String email;
    private String sub;
}