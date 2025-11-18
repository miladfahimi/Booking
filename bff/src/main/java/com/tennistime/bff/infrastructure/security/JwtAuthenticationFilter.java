package com.tennistime.bff.infrastructure.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennistime.bff.infrastructure.feign.AuthServiceClient;
import com.tennistime.bff.redis.TokenCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private TokenCacheService tokenCacheService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            Map<String, Object> tokenInfo = null;
            String cachedResponse = tokenCacheService.getTokenValidation(jwt);

            if (cachedResponse != null) {
                tokenInfo = parseCachedResponse(cachedResponse);
                logger.info("\033[1;36m[JwtAuthenticationFilter] Cache hit for token: {}\033[0m", jwt);
            } else {
                try {
                    Map<String, String> requestMap = Map.of("token", jwt);
                    tokenInfo = authServiceClient.validateToken(requestMap);

                    // Logging the response
                    logger.info("\033[1;36m----------------------------\033[0m");
                    logger.info("\033[1;36m[JwtAuthenticationFilter] Token Info: {}\033[0m", tokenInfo);
                    logger.info("\033[1;36m----------------------------\033[0m");

                    // Cache the validation result
                    tokenCacheService.cacheTokenValidation(jwt, tokenInfo);
                } catch (Exception e) {
                    logger.error("\033[1;31m----------------------------\033[0m");
                    logger.error("\033[1;31m[JwtAuthenticationFilter] Token validation error: {}\033[0m", e.getMessage());
                    logger.error("\033[1;31m----------------------------\033[0m");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            if (tokenInfo != null && Boolean.TRUE.equals(tokenInfo.get("valid"))) {
                String username = (String) tokenInfo.get("username");
                String email = (String) tokenInfo.get("email");
                List<String> roles = (List<String>) tokenInfo.get("roles");
                UUID userId = extractUserId(tokenInfo.get("userId"));

                // Ensure roles are prefixed with "ROLE_"
                roles = roles.stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .collect(Collectors.toList());

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, username, email, roles);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        authenticatedUser, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Logging
                logger.info("\033[1;32m----------------------------\033[0m");
                logger.info("\033[1;32m[JwtAuthenticationFilter] Token validated and user authenticated: {}\033[0m", username);
                logger.info("\033[1;32mEmail: {}\033[0m", email);
                logger.info("\033[1;32mRoles: {}\033[0m", roles);
                logger.info("\033[1;32m----------------------------\033[0m");
            } else {
                // Logging
                logger.info("\033[1;31m----------------------------\033[0m");
                logger.info("\033[1;31m[JwtAuthenticationFilter] Invalid token: {}\033[0m", jwt);
                logger.info("\033[1;31m----------------------------\033[0m");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private Map<String, Object> parseCachedResponse(String cachedResponse) {
        // Parse the cached response string back to a Map
        try {
            return objectMapper.readValue(cachedResponse, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("\033[1;31m----------------------------\033[0m");
            logger.error("\033[1;31m[JwtAuthenticationFilter] Error parsing cached response: {}\033[0m", e.getMessage());
            logger.error("\033[1;31m----------------------------\033[0m");
            return null;
        }
    }

    private UUID extractUserId(Object userIdValue) {
        if (userIdValue instanceof String userIdString && !userIdString.isBlank()) {
            try {
                return UUID.fromString(userIdString);
            } catch (IllegalArgumentException ignored) {
                logger.warn("\033[1;33m[JwtAuthenticationFilter] Unable to parse userId from token.\033[0m");
            }
        }
        return null;
    }
}
