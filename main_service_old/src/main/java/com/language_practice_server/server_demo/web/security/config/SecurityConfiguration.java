package com.language_practice_server.server_demo.web.security.config;

import com.language_practice_server.server_demo.web.security.handler.OAuth2AuthenticationSuccessHandler;
import com.language_practice_server.server_demo.web.security.service.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    private final JwtTokenProvider tokenProvider;

    public SecurityConfiguration(OAuth2AuthenticationSuccessHandler oauth2SuccessHandler,
                                 OAuth2AuthorizedClientService authorizedClientService, JwtTokenProvider tokenProvider) {
        this.oauth2SuccessHandler = oauth2SuccessHandler;
        this.authorizedClientService = authorizedClientService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173") // або props.frontend allowed origins
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(tokenProvider);
        http.authorizeHttpRequests(
                (requests) -> requests
                        .requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/favicon.ico").permitAll()
                        .requestMatchers("/api/oauth2/**", "/api/auth/**", "/public/**", "/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/auth/**", "/vacancies", "/about", "/forStudents", "/forTeachers", "/error").permitAll()
                        .requestMatchers("/tasktemplate/**", "/tasks/**", "/users/**", "/persons/**").authenticated()
        )
                .exceptionHandling(handler -> handler.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .addFilterBefore(jwtFilter, OAuth2LoginAuthenticationFilter.class)
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
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                );

        http.csrf(csrf -> csrf.disable());
        http.formLogin(form -> form.disable());
        return http.build();
    }
}
