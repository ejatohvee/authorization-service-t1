package org.ejatohvee.authorizationservicet1.service;

import lombok.RequiredArgsConstructor;
import org.ejatohvee.authorizationservicet1.entity.RefreshToken;
import org.ejatohvee.authorizationservicet1.entity.User;
import org.ejatohvee.authorizationservicet1.repository.RefreshTokenRepository;
import org.ejatohvee.authorizationservicet1.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Value("${jwt.expiration-ms}")
    private long accessExpirationMs;

    public String generateAccessToken(User user) {
        return jwtUtil.generateToken(user.getUsername());
    }

    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public void revokeRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }

    public long getAccessTokenTtl() {
        return accessExpirationMs;
    }
}
