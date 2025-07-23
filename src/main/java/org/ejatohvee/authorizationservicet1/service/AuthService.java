package org.ejatohvee.authorizationservicet1.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ejatohvee.authorizationservicet1.entity.RefreshToken;
import org.ejatohvee.authorizationservicet1.entity.User;
import org.ejatohvee.authorizationservicet1.model.AuthRequest;
import org.ejatohvee.authorizationservicet1.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public ResponseEntity<?> login(@Valid AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.createRefreshToken(user);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken.getToken()));
    }

    public ResponseEntity<?> refresh(String refreshTokenValue) {
        RefreshToken refreshToken = tokenService.findByToken(refreshTokenValue).orElseThrow();

        if (tokenService.isRefreshTokenExpired(refreshToken)) {
            tokenService.revokeRefreshToken(refreshToken.getUser());
            return ResponseEntity.status(403).body("Refresh token expired");
        }

        String newAccessToken = tokenService.generateAccessToken(refreshToken.getUser());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    public ResponseEntity<?> logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing Authorization header");
        }

        String token = authHeader.substring(7);
        String username = tokenService.getUsernameFromToken(token);
        userRepository.findByUsername(username).ifPresent(tokenService::revokeRefreshToken);

        long ttl = Duration.ofMillis(tokenService.getAccessTokenTtl()).toMillis();
        tokenBlacklistService.blacklistToken(token, ttl);

        return ResponseEntity.ok("Logged out");
    }
}
