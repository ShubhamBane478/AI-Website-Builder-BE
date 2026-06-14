package com.hustlerdev.aiwebsitebuilder.service;

import com.hustlerdev.aiwebsitebuilder.entity.RefreshToken;
import com.hustlerdev.aiwebsitebuilder.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiry-ms}")
    private long jwtExpiryMs;

    @Value("${jwt.refresh-expiry-ms}")
    private long refreshExpiryMs;

    // --- Access Token (short-lived JWT, sent in Authorization header) ---

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())       // who the token belongs to
                .claim("email", user.getEmail())         // extra data packed inside
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .signWith(getSignKey())                  // signs it so it can't be faked
                .compact();
    }

    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token); // throws if expired or tampered
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Refresh Token (long-lived random string, stored in DB + httpOnly cookie) ---

    public RefreshToken generateRefreshToken(User user) {
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())    // random, not a JWT
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpiryMs / 1000))
                .build();
    }

    // --- Internal helpers ---

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
