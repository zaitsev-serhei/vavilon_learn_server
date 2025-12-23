package com.language_practice_server.server_demo.web.security.config;

import com.language_practice_server.server_demo.web.security.handler.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Main security configuration class.
 * Defines security rules for HTTP requests, permitted endpoints, and filter chain.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public SecurityConfiguration(OAuth2AuthenticationSuccessHandler oauth2SuccessHandler,
                                 OAuth2AuthorizedClientService authorizedClientService) {
        this.oauth2SuccessHandler = oauth2SuccessHandler;
        this.authorizedClientService = authorizedClientService;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers(HttpMethod.GET,
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",
                                        "/webjars/**",
                                        "/favicon.ico").permitAll()
                                .requestMatchers(
                                        "/api/oauth2/**",
                                        "/api/auth/**",
                                        "/oauth2/**",
                                        "/login/**").permitAll()
                                .requestMatchers("/internal/**").hasAuthority("SCOPE_internal")
                                .anyRequest().denyAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizedClientService(authorizedClientService)
                        .successHandler(oauth2SuccessHandler)
                        .failureHandler((req, res, ex) -> {
                            // редіректимо на фронт з повідомленням про помилку
                            String redirect = "http://localhost:5173/oauth2/callback/google?oauth_error=" +
                                    URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
                            res.sendRedirect(redirect);
                        })
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt
                                .jwkSetUri("http://auth-service/.well-known/jwks.json")
                        )
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                );
        return http.build();
    }
}
