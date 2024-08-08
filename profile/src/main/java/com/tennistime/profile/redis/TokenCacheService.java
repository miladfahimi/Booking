package com.tennistime.profile.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {

    private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void cacheTokenValidation(String token, Map<String, Object> tokenInfo) {
        try {
            String jsonString = objectMapper.writeValueAsString(tokenInfo);
            redisTemplate.opsForValue().set(token, jsonString, EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Add proper error handling here
        }
    }

    public String getTokenValidation(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void invalidateToken(String token) {
        redisTemplate.delete(token);
    }
}
