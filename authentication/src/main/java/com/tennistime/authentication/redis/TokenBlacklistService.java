package com.tennistime.authentication.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class TokenBlacklistService {
    private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public void blacklistToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set(token, "BLACKLISTED", EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return "BLACKLISTED".equals(redisTemplate.opsForValue().get(token));
    }
}
