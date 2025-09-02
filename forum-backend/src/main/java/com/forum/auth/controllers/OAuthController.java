package com.forum.auth.controllers;

import com.forum.auth.services.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/login/google")
    public void loginWithGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuthService.buildAuthUrl());
    }

    @GetMapping("/callback/google")
    public ResponseEntity<Map<String, String>> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok(oAuthService.handleCallback(code));
    }
}


