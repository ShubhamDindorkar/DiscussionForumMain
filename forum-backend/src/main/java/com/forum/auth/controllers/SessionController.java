package com.forum.auth.controllers;

import com.forum.auth.repo.RefreshTokenRepository;
import com.forum.auth.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class SessionController {
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public SessionController(TokenService tokenService, RefreshTokenRepository refreshTokenRepository) {
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        return ResponseEntity.ok(tokenService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Principal principal, @RequestBody(required = false) Map<String, String> body) {
        if (body != null && body.containsKey("refreshToken")) {
            tokenService.revoke(body.get("refreshToken"));
        } else if (principal != null) {
            tokenService.revokeAllForUser(principal.getName());
        }
        return ResponseEntity.noContent().build();
    }
}


