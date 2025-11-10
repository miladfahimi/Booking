package com.tennistime.authentication.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes) long.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof CustomUserDetails)) {
            throw new IllegalArgumentException("UserDetails must be instance of CustomUserDetails");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        if (customUserDetails.getEmail() != null) {
            claims.put("email", customUserDetails.getEmail());
        }
        if (customUserDetails.getCustomUsername() != null) {
            claims.put("username", customUserDetails.getCustomUsername());
        }
        if (customUserDetails.getPhone() != null) {
            claims.put("phone", customUserDetails.getPhone());
        }
        if (customUserDetails.getId() != null) {
            claims.put("userId", customUserDetails.getId().toString());
        }

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        // Logging
        System.out.println("\033[1;35m----------------------------\033[0m");
        System.out.println("\033[1;35m[JwtUtil] Generated JWT Token: " + token + "\033[0m");
        System.out.println("\033[1;35m----------------------------\033[0m");

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);

            // Logging
            System.out.println("\033[1;36m----------------------------\033[0m");
            System.out.println("\033[1;36m[JwtUtil] Token validated: " + token + "\033[0m");
            System.out.println("\033[1;36m----------------------------\033[0m");

            return true;
        } catch (Exception e) {
            // Logging
            System.out.println("\033[1;31m----------------------------\033[0m");
            System.out.println("\033[1;31m[JwtUtil] Token validation failed: " + token + "\033[0m");
            System.out.println("\033[1;31m----------------------------\033[0m");
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("email", String.class));
    }

    public String getCustomUsernameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("username", String.class));
    }

    public String getPhoneFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("phone", String.class));
    }

    public UUID getUserIdFromToken(String token) {
        String userId = getClaimFromToken(token, claims -> claims.get("userId", String.class));
        return userId != null ? UUID.fromString(userId) : null;
    }

    public List<String> getRolesFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("roles", List.class));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }
}
