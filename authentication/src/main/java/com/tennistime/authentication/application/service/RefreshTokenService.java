package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.RefreshToken;
import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refreshExpirationMs.standard:43200000}")
    private long standardRefreshExpirationMs;

    @Value("${jwt.refreshExpirationMs.rememberMe:1209600000}")
    private long rememberMeRefreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Creates and persists a refresh token for the provided user.
     *
     * @param user the authenticated user
     * @return the persisted refresh token instance
     */
    public RefreshToken createRefreshToken(User user, boolean rememberMe) {
        LocalDateTime now = LocalDateTime.now();
        long expiration = rememberMe ? rememberMeRefreshExpirationMs : standardRefreshExpirationMs;
        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateSecureToken())
                .user(user)
                .expiryDate(now.plus(Duration.ofMillis(expiration)))
                .revoked(Boolean.FALSE)
                .rememberMe(rememberMe)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Rotates the provided refresh token if it is valid and returns the new token.
     *
     * @param token the provided refresh token value
     * @return a newly persisted refresh token linked to the same user
     */
    @Transactional
    public RefreshToken rotateRefreshToken(String token) {
        RefreshToken existingToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        LocalDateTime now = LocalDateTime.now();
        if (Boolean.TRUE.equals(existingToken.getRevoked()) || existingToken.getExpiryDate().isBefore(now)) {
            existingToken.setRevoked(Boolean.TRUE);
            existingToken.setUpdatedAt(now);
            refreshTokenRepository.save(existingToken);
            throw new IllegalArgumentException("Refresh token is expired or revoked");
        }

        existingToken.setRevoked(Boolean.TRUE);
        existingToken.setUpdatedAt(now);
        refreshTokenRepository.save(existingToken);
        return createRefreshToken(existingToken.getUser(), Boolean.TRUE.equals(existingToken.getRememberMe()));
        }

    /**
     * Revokes all refresh tokens associated with the provided user.
     *
     * @param user the user whose refresh tokens should be revoked
     */
    @Transactional
    public void revokeTokens(User user) {
        LocalDateTime now = LocalDateTime.now();
        refreshTokenRepository.findAllByUser(user).forEach(token -> {
            token.setRevoked(Boolean.TRUE);
            token.setUpdatedAt(now);
            refreshTokenRepository.save(token);
        });
    }

    /**
     * Generates a cryptographically strong token string for refresh tokens.
     *
     * @return the generated token value
     */
    private String generateSecureToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
