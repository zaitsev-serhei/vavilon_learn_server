package com.language_practice_server.server_demo.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkUrl;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(
                        ex -> ex
                                .pathMatchers(
                                        "/oauth2/**",
                                        " /api/oauth2/**",
                                        "login/oauth2/**",
                                        "/.well-known/**",
                                        "/actuator/**")
                                .permitAll()
                                .pathMatchers("/api/**").hasAuthority("SCOPE_USER")
                                .pathMatchers("/internal/**").hasAuthority("SCOPE_internal")
                                .anyExchange().denyAll()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
