package com.forum.auth.services;

import com.forum.auth.JwtUtil;
import com.forum.auth.model.RefreshToken;
import com.forum.auth.repo.RefreshTokenRepository;
import com.forum.auth.repo.UserRepository;
import com.forum.core.AuditLog;
import com.forum.auth.repo.AuditLogRepository;
import com.forum.core.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuditLogRepository auditLogRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtUtil jwtUtil, AuditLogRepository auditLogRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public Map<String, String> refresh(String token) {
        RefreshToken rt = refreshTokenRepository.findByTokenAndRevokedFalse(token).orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        if (rt.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Refresh token expired");
        }
        User user = userRepository.findById(rt.getUserId()).orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roles", user.getRoles().stream().map(r -> r.getName().name()).toList());
        String access = jwtUtil.generateAccessToken(user.getEmail(), claims);
        AuditLog log = new AuditLog();
        log.setAction("token_refresh");
        log.setActorId(user.getId());
        auditLogRepository.save(log);
        return Map.of("accessToken", access);
    }

    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByTokenAndRevokedFalse(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    @Transactional
    public void revokeAllForUser(String email) {
        userRepository.findByEmail(email).ifPresent(user -> refreshTokenRepository.deleteByUserId(user.getId()));
    }
}


