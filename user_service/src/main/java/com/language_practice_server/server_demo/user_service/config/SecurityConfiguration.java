package com.language_practice_server.server_demo.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/actuator/**").permitAll()
                //auth-service will provide internal short-live JWT for per-service communication with internal authority
                .anyRequest().hasAuthority("SCOPE_internal")
        ).oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults())
        ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
