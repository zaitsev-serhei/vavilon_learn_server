package com.language_practice_server.server_demo.config;

import com.language_practice_server.server_demo.web.security.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceAuditConfig {
    @Bean
    public AuditorAware<Long> auditorProvider(){
        //return new SystemAuditorAware();
        return new SecurityAuditorAware(); // activate when security stuff is ready
    }
}
