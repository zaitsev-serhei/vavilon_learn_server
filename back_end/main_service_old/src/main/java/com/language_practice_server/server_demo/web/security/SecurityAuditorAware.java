package com.language_practice_server.server_demo.web.security;

import com.language_practice_server.server_demo.web.security.config.JwtAuthenticationFilter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityAuditorAware implements AuditorAware<Long> {
    private final Logger logger = LoggerFactory.getLogger(SecurityAuditorAware.class);

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        logger.debug("PRINCIPAL: {}", principal.toString());
        if (principal instanceof JwtAuthenticationFilter.AuthenticatedUser) {
            return Optional.of(((JwtAuthenticationFilter.AuthenticatedUser) principal).userId());
        }
        return Optional.empty();
    }
}
