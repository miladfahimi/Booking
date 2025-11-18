package com.tennistime.bff.application.util;

import com.tennistime.bff.infrastructure.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides information about the authenticated user stored in the security context.
 */
@Component
public class CurrentUserProvider {

    /**
     * Retrieves the identifier of the authenticated user when available.
     *
     * @return an optional containing the current user identifier
     */
    public Optional<UUID> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return Optional.ofNullable(authenticatedUser.getUserId());
        }

        return Optional.empty();
    }
}
