package com.language_practice_server.server_demo.web.security;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SystemAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(123L); //for system user test
    }
}
