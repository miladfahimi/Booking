package com.tennistime.reservation.application.controller;

import com.tennistime.reservation.redis.TokenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private TokenCacheService tokenCacheService;

    /**
     * Invalidate a token by removing it from the cache.
     *
     * @param token The token to be invalidated. This is a request parameter passed in the URL.
     * @return A response entity with an HTTP status of 200 OK, indicating that the token was successfully invalidated.
     */
    @DeleteMapping("/invalidate")
    public ResponseEntity<Void> invalidateToken(@RequestParam String token) {
        tokenCacheService.invalidateToken(token);
        return ResponseEntity.ok().build();
    }
}
