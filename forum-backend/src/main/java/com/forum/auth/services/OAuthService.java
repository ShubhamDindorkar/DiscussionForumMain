package com.forum.auth.services;

import com.forum.auth.JwtUtil;
import com.forum.auth.model.RefreshToken;
import com.forum.auth.repo.AuditLogRepository;
import com.forum.auth.repo.RefreshTokenRepository;
import com.forum.auth.repo.RoleRepository;
import com.forum.auth.repo.UserRepository;
import com.forum.core.AppRole;
import com.forum.core.AuditLog;
import com.forum.core.Role;
import com.forum.core.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class OAuthService {

    private final String clientId;
    private final String clientSecret;
    private final String allowedDomain;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final AuditLogRepository auditLogRepository;

    public OAuthService(
        @Value("${GOOGLE_OAUTH_CLIENT_ID:}") String clientId,
        @Value("${GOOGLE_OAUTH_CLIENT_SECRET:}") String clientSecret,
        @Value("${app.security.allowedEmailDomain}") String allowedDomain,
        JwtUtil jwtUtil,
        UserRepository userRepository,
        RefreshTokenRepository refreshTokenRepository,
        RoleRepository roleRepository,
        AuditLogRepository auditLogRepository
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.allowedDomain = allowedDomain;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.roleRepository = roleRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public String buildAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + clientId +
            "&redirect_uri=" + urlEncode(callbackUrl()) +
            "&scope=" + urlEncode("openid email profile");
    }

    private String callbackUrl() {
        return "http://localhost:8080/api/auth/callback/google";
    }

    private String urlEncode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Transactional
    public Map<String, String> handleCallback(String code) {
        // NOTE: In real implementation, exchange code with Google token endpoint and fetch userinfo.
        // For scaffold, simulate user info from code value.
        String email = code + "@" + allowedDomain; // placeholder for tests
        String name = "Google User";
        String picture = "";
        String sub = UUID.randomUUID().toString();

        enforceDomain(email);

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(null);
        user.setAvatarUrl(picture);
        user.setProviderId(sub);
        // assign roles: ADMIN if email in whitelist, else STUDENT
        boolean isAdmin = isWhitelistedAdmin(email);
        Role roleName = isAdmin ? Role.ADMIN : Role.STUDENT;
        AppRole role = roleRepository.findByName(roleName).orElseGet(() -> {
            AppRole r = new AppRole();
            r.setName(roleName);
            return r;
        });
        user.getRoles().clear();
        user.getRoles().add(role);
        user = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roles", user.getRoles().stream().map(r -> r.getName().name()).toList());
        String access = jwtUtil.generateAccessToken(user.getEmail(), claims);

        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setUserId(user.getId());
        rt.setExpiresAt(OffsetDateTime.now().plusDays(7));
        refreshTokenRepository.save(rt);

        AuditLog log = new AuditLog();
        log.setAction("oauth_login");
        log.setActorId(user.getId());
        auditLogRepository.save(log);

        return Map.of("accessToken", access, "refreshToken", rt.getToken());
    }

    private void enforceDomain(String email) {
        if (!email.toLowerCase(Locale.ROOT).endsWith("@" + allowedDomain.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Email domain not allowed");
        }
    }
}


