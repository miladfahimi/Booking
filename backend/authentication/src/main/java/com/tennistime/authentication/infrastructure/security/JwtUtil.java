package com.tennistime.authentication.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    @Autowired
    private AppUserRepository appUserRepository;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes) long.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        String token = Jwts.builder()
                .setSubject(email)
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
            JwtParser parser = Jwts.parser().setSigningKey(getSigningKey()).build();
            parser.parseClaimsJws(token);

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

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser().setSigningKey(getSigningKey()).build();
        return parser.parseClaimsJws(token).getBody();
    }

    public AppUser extractUserFromToken(String token) {
        String email = getUsernameFromToken(token);
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        return appUserOptional.orElse(null);
    }
}
