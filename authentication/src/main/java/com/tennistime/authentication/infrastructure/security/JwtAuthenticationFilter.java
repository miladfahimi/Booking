package com.tennistime.authentication.infrastructure.security;

import com.tennistime.authentication.redis.TokenBlacklistService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String email = null;
        UUID userId = null;
        String jwt = null;
        List<String> roles = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    logger.error("\033[1;31m----------------------------\033[0m");
                    logger.error("\033[1;31m[JwtAuthenticationFilter] Token is blacklisted: {}\033[0m", jwt);
                    logger.error("\033[1;31m----------------------------\033[0m");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                username = jwtUtil.getUsernameFromToken(jwt);
                email = jwtUtil.getEmailFromToken(jwt);
                roles = jwtUtil.getRolesFromToken(jwt);
                userId = jwtUtil.getUserIdFromToken(jwt);

                // Logging all claims
                logger.info("\033[1;34m----------------------------\033[0m");
                logger.info("\033[1;34mJWT Token: {}\033[0m", jwt);
                logger.info("\033[1;34mUser ID: {}\033[0m", userId);
                logger.info("\033[1;34mUsername: {}\033[0m", username);
                logger.info("\033[1;34mEmail: {}\033[0m", email);
                logger.info("\033[1;34mRoles: {}\033[0m", roles);
                logger.info("\033[1;34m----------------------------\033[0m");

            } catch (Exception e) {
                logger.error("\033[1;31mJWT parsing error: {}\033[0m", e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                if (userId != null) {
                    request.setAttribute("authenticatedUserId", userId);
                }
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                // Logging
                logger.info("\033[1;32m----------------------------\033[0m");
                logger.info("\033[1;32mAuthenticated user: {}\033[0m", username);
                logger.info("\033[1;32mUser ID: {}\033[0m", userId);
                logger.info("\033[1;32mEmail: {}\033[0m", email);
                logger.info("\033[1;32mRoles: {}\033[0m", roles);
                logger.info("\033[1;32m----------------------------\033[0m");
            } else {
                logger.info("\033[1;31m----------------------------\033[0m");
                logger.info("\033[1;31mToken is invalid\033[0m");
                logger.info("\033[1;31m----------------------------\033[0m");
            }
        }

        chain.doFilter(request, response);
    }
}
