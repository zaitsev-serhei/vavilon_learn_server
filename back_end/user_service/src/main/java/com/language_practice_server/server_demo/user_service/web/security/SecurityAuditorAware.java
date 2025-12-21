package com.language_practice_server.server_demo.user_service.web.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<Long> {
    private final Logger logger = LoggerFactory.getLogger(SecurityAuditorAware.class);

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return Optional.empty();
        }
        //update this for Java 13+
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Long userId = jwt.getClaim("uid");
        if (userId == null) {
            logger.debug("JWT token does not contain user id claim");
            return Optional.empty();
        }
        return Optional.of(userId);
    }
}
