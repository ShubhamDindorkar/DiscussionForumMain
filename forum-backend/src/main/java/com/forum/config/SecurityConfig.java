package com.forum.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.security.jwt.public-key-location}")
    private Resource publicKeyLocation;

    @Value("${app.security.jwt.private-key-location}")
    private Resource privateKeyLocation;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry -> registry
                .requestMatchers("/api/health", "/actuator/health", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/ws/**", "/stomp/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.addFilterBefore(new com.forum.auth.TokenFilter(jwtDecoder()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = com.forum.config.crypto.KeyLoader.loadPublicKey(publicKeyLocation);
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    @ConditionalOnResource(resources = "classpath:keys/jwt.key")
    public JwtEncoder jwtEncoder() throws Exception {
        RSAPublicKey publicKey = com.forum.config.crypto.KeyLoader.loadPublicKey(publicKeyLocation);
        RSAPrivateKey privateKey = com.forum.config.crypto.KeyLoader.loadPrivateKey(privateKeyLocation);
        RSAKey jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new com.nimbusds.jose.jwk.JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}


