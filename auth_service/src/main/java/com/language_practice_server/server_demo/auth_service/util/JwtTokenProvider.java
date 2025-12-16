package com.language_practice_server.server_demo.auth_service.util;

import com.language_practice_server.server_demo.auth_service.client.UserServiceClient;
import com.language_practice_server.server_demo.auth_service.common.TokenType;
import com.language_practice_server.server_demo.auth_service.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Provides utility methods for generating and validating JWT tokens.
 * Uses secret key and claims to create tokens for authenticated users.
 */

@Component
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserServiceClient userServiceClient;
    private final JwtKeyProvider keyProvider;
    private final JwtParser jwtParser;
    private final long accessTokenExpr;
    private final long serviceTokenExpr;

    public JwtTokenProvider(UserServiceClient userServiceClient,
                            JwtKeyProvider keyProvider,
                            @Value("${security.jwt.access-token-exp}") long accessTokenExpr,
                            @Value("${security.jwt.service-token-exp}") long serviceTokenExpr) {
        this.userServiceClient = userServiceClient;
        this.keyProvider = keyProvider;
        this.accessTokenExpr = accessTokenExpr;
        this.serviceTokenExpr = serviceTokenExpr;
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(keyProvider.getPublicKey())
                .build();
    }


    public String createUserAccessToken(Long userId) {
        logger.debug("Creating new access token for userId: {}", userId);
        try {
            UserDto user = userServiceClient.findUserById(userId);

            Instant now = Instant.now();
            Instant exp = now.plusMillis(accessTokenExpr);

            return Jwts.builder()
                    .setHeaderParam("kid", keyProvider.getKeyId())
                    .setSubject(String.valueOf(user.getUserId()))
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(exp))
                    .claim("uid", user.getUserId())
                    //TODO: consider better option to use name/full name
                    .claim("name", user.getFirstName() + " " + user.getLastName())
                    .claim("roles", user.getRole())
                    .claim("type", TokenType.USER)
                    .signWith(keyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception exception) {
            logger.error("Failed to find user in User Service to create access token. UserId: {}", userId);
            return "";
        }
    }

    public String createServiceAccessToken(String serviceName) {
        logger.debug("Creating new access token for service: {}", serviceName);
        Instant now = Instant.now();
        Instant exp = now.plusMillis(accessTokenExpr);
        return Jwts.builder()
                .setHeaderParam("kid", keyProvider.getKeyId())
                .setSubject(serviceName)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("scope", "internal")
                .claim("type", TokenType.SERVICE)
                .signWith(keyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
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

    public boolean isUserToken(Claims claims){
        return TokenType.USER.equals(claims.get("type"));
    }
}
