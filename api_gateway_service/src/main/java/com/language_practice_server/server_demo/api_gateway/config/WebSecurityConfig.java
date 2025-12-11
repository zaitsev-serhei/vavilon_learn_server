package com.language_practice_server.server_demo.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkUrl;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        logger.debug("Creating Nimbus decoder for JWKS set {}", jwkUrl);
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkUrl).build();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(
                ex -> ex
                        .pathMatchers("/api/oauth2/**", "/oauth2/**", "/login/oauth2/**",
                                "/.well-known/jwks.json", "/public/**", "/actuator/**")
                        .permitAll()
                        .anyExchange().authenticated()
        )
                .oauth2ResourceServer(spec -> spec.jwt(jwtSpec -> {
                    //spring can create its own decoder
                }));
        // .jwt(jwt -> jwt.jwkSetUri(jwkUrl))

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    // Optional: propagate principal headers downstream
    @Bean
    public WebFilter addPrincipalHeadersFilter() {
        return (exchange, chain) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Map<String, Object> claims = ((Jwt) auth.getPrincipal()).getClaims();
                if (claims.containsKey("uid")) {
                    exchange.getRequest().mutate()
                            .header("X-User-Id", claims.get("uid").toString())
                            .header("X-User-Roles", claims.getOrDefault("roles", "").toString())
                            .build();
                }
            }
            return chain.filter(exchange);
        };
    }
}
