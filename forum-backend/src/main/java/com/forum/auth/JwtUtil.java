package com.forum.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
public class JwtUtil {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final String audience;
    private final String accessTtl;

    public JwtUtil(JwtEncoder jwtEncoder,
                   @Value("${app.security.jwt.issuer}") String issuer,
                   @Value("${app.security.jwt.audience}") String audience,
                   @Value("${app.security.jwt.accessTtl}") String accessTtl) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.audience = audience;
        this.accessTtl = accessTtl;
    }

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        long minutes = parseMinutes(accessTtl);
        Instant now = Instant.now();
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
            .issuer(issuer)
            .issuedAt(now)
            .expiresAt(now.plus(minutes, ChronoUnit.MINUTES))
            .subject(subject)
            .audience(java.util.List.of(audience));
        if (claims != null) {
            claims.forEach(builder::claim);
        }
        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build())).getTokenValue();
    }

    private long parseMinutes(String ttl) {
        if (ttl.endsWith("m")) {
            return Long.parseLong(ttl.substring(0, ttl.length() - 1));
        }
        if (ttl.endsWith("h")) {
            return Long.parseLong(ttl.substring(0, ttl.length() - 1)) * 60;
        }
        if (ttl.endsWith("d")) {
            return Long.parseLong(ttl.substring(0, ttl.length() - 1)) * 60 * 24;
        }
        return Long.parseLong(ttl);
    }
}


