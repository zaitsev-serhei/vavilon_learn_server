package com.language_practice_server.server_demo.web.security.service;

import com.language_practice_server.server_demo.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


/**
 * Provides utility methods for generating and validating JWT tokens.
 * Uses secret key and claims to create tokens for authenticated users.
 */

@Component
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${security.jwt.secret-key}")
    private String jwtSecret;
    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;
    private Key hmacKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            logger.warn("JWT secret is not set or too short. Please set a strong secret (>=32 chars) in .env file");
        }
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        this.hmacKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build();
    }

    public String createAccessToken(User user) {
        logger.debug("Creating new access token for userId: {}", user.getId());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration);
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        if (user.getRole() != null) {
            claims.put("roles", user.getRole());
        }
        if (!user.getUserName().isEmpty()) {
            claims.put("name", user.getUserName());
        }
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(hmacKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.debug("JWT expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported JWT: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.warn("Malformed JWT: {}", ex.getMessage());
        } catch (SecurityException ex) {
            logger.warn("Invalid JWT signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.warn("JWT token is invalid: {}", ex.getMessage());
        }
        return false;
    }

    public Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(jwtParser.parseClaimsJws(token).getBody());
        } catch (JwtException ex) {
            logger.debug("Error parsing JWT claims: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getUserNameFromJwt(String token) {
        return parseClaims(token).map(claims -> {
            Object uid = claims.get("name");
            if (uid == null) return null;
            if (uid instanceof String) return uid.toString();
            try {
                return (uid.toString());
            } catch (Exception e) {
                return null;
            }
        });
    }

    public Optional<Long> getUserIdFromJwt(String token) {
        return parseClaims(token).map(claims -> {
            Object uid = claims.get("uid");
            if (uid == null) return null;
            if (uid instanceof Number) return ((Number) uid).longValue();
            try {
                return Long.parseLong(uid.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    public List<SimpleGrantedAuthority> getRolesFromJwt(String token) {
        return parseClaims(token)
                .map(claims -> {
                    Object r = claims.get("roles");
                    if (r == null) return Collections.<SimpleGrantedAuthority>emptyList();
                    String csv = String.valueOf(r);
                    return Arrays.stream(csv.split(","))
                            .filter(s -> !s.isBlank())
                            .map(String::trim)
                            .map(SimpleGrantedAuthority::new) // roles should be prefixed with ROLE_ if using hasRole
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    public Optional<Date> getExpiration(String token) {
        return parseClaims(token).map(Claims::getExpiration);
    }
}
