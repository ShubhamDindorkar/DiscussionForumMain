package com.forum.auth;

import com.forum.auth.repo.AuditLogRepository;
import com.forum.auth.repo.RefreshTokenRepository;
import com.forum.auth.repo.UserRepository;
import com.forum.auth.services.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OAuthServiceTest {

    private OAuthService service;

    @BeforeEach
    void setup() {
        JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
        Mockito.when(jwtUtil.generateAccessToken(Mockito.anyString(), Mockito.anyMap())).thenReturn("access");
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RefreshTokenRepository refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        AuditLogRepository auditLogRepository = Mockito.mock(AuditLogRepository.class);
        service = new OAuthService("id","secret","example.edu", jwtUtil, userRepository, refreshTokenRepository, auditLogRepository);
    }

    @Test
    void rejectsWrongDomain() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handleCallback("user@gmail.com"));
        assertTrue(ex.getMessage().toLowerCase().contains("domain"));
    }
}


