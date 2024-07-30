package com.tennistime.backend.infrastructure.security;

import com.tennistime.common.security.TokenBlacklistService;
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
                username = jwtUtil.extractUsername(jwt);
                roles = jwtUtil.extractRoles(jwt);

                // Logging all claims
                logger.info("\033[1;34m----------------------------\033[0m");
                logger.info("\033[1;34mJWT Token: {}\033[0m", jwt);
                logger.info("\033[1;34mUsername: {}\033[0m", username);
                logger.info("\033[1;34mRoles: {}\033[0m", roles);
                logger.info("\033[1;34m----------------------------\033[0m");

            } catch (Exception e) {
                logger.error("\033[1;31mJWT parsing error: {}\033[0m", e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, username)) {
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                // Logging
                logger.info("\033[1;32m----------------------------\033[0m");
                logger.info("\033[1;32mAuthenticated user: {}\033[0m", username);
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
