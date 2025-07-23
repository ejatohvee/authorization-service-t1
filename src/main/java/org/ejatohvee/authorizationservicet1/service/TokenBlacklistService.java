package org.ejatohvee.authorizationservicet1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "blacklist:";

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(PREFIX + token, "true", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + token));
    }
}