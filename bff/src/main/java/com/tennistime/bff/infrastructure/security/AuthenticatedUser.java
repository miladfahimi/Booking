package com.tennistime.bff.infrastructure.security;

import java.util.List;
import java.util.UUID;

/**
 * Represents the authenticated user extracted from the JWT token.
 */
public class AuthenticatedUser {

    private final UUID userId;
    private final String username;
    private final String email;
    private final List<String> roles;

    public AuthenticatedUser(UUID userId, String username, String email, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
