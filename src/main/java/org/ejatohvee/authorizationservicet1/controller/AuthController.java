package org.ejatohvee.authorizationservicet1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ejatohvee.authorizationservicet1.model.AuthRequest;
import org.ejatohvee.authorizationservicet1.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        return authService.refresh(body.get("refreshToken"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        return authService.logout(authHeader);
    }
}
