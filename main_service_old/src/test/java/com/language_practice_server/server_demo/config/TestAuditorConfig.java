package com.language_practice_server.server_demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@TestConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class TestAuditorConfig {
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> Optional.of(111L);
    }
}
