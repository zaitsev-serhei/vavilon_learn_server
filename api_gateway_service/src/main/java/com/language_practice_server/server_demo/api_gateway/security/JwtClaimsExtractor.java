package com.language_practice_server.server_demo.api_gateway.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class JwtClaimsExtractor {
    public Optional<Long> extractUserId(Jwt jwt){
        return Optional.ofNullable(jwt.getClaim("uid")).map(v->Long.valueOf(v.toString()));
    }
    public Optional<String> extractRole(Jwt jwt){
        return Optional.ofNullable(jwt.getClaim("role")).map(Object::toString);
    }
    public boolean isUserToken(Jwt jwt){
        return "USER".equals(String.valueOf(jwt.getClaim("type")));
    }
}
