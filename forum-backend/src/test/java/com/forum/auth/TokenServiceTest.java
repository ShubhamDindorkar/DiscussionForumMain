package com.forum.auth;

import com.forum.auth.model.RefreshToken;
import com.forum.auth.repo.AuditLogRepository;
import com.forum.auth.repo.RefreshTokenRepository;
import com.forum.auth.repo.UserRepository;
import com.forum.auth.services.TokenService;
import com.forum.core.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {
    @Test
    void refreshReturnsNewAccessToken() {
        RefreshTokenRepository refreshRepo = Mockito.mock(RefreshTokenRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
        AuditLogRepository auditRepo = Mockito.mock(AuditLogRepository.class);

        RefreshToken rt = new RefreshToken();
        rt.setToken("t");
        rt.setUserId(1L);
        rt.setExpiresAt(OffsetDateTime.now().plusDays(1));
        Mockito.when(refreshRepo.findByTokenAndRevokedFalse("t")).thenReturn(Optional.of(rt));
        User user = new User();
        user.setEmail("u@example.edu");
        user.setId(1L);
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateAccessToken(Mockito.anyString(), Mockito.anyMap())).thenReturn("access");

        TokenService service = new TokenService(refreshRepo, userRepo, jwtUtil, auditRepo);
        Map<String, String> out = service.refresh("t");
        assertEquals("access", out.get("accessToken"));
    }
}


