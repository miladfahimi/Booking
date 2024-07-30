package com.tennistime.authentication.infrastructure.security;

import com.tennistime.common.security.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

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
                    System.out.println("\033[1;31m----------------------------\033[0m");
                    System.out.println("\033[1;31m[JwtAuthenticationFilter] Token is blacklisted: " + jwt + "\033[0m");
                    System.out.println("\033[1;31m----------------------------\033[0m");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                username = jwtUtil.getUsernameFromToken(jwt);
                roles = jwtUtil.getRolesFromToken(jwt);

                // Logging
                System.out.println("\033[1;34m----------------------------\033[0m");
                System.out.println("\033[1;34mJWT Token: " + jwt + "\033[0m");
                System.out.println("\033[1;34mUsername: " + username + "\033[0m");
                System.out.println("\033[1;34mRoles: " + roles + "\033[0m");
                System.out.println("\033[1;34m----------------------------\033[0m");
            } catch (Exception e) {
                logger.warn("Cannot set user authentication: " + e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // Logging
                System.out.println("\033[1;32m----------------------------\033[0m");
                System.out.println("\033[1;32m[JwtAuthenticationFilter] Authenticated user: " + username + "\033[0m");
                System.out.println("\033[1;32mRoles: " + roles + "\033[0m");
                System.out.println("\033[1;32m----------------------------\033[0m");
            }
        }

        chain.doFilter(request, response);
    }
}
