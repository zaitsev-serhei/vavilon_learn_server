package com.language_practice_server.server_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import javax.sql.DataSource;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(DataSource dataSource,
                                                                 ClientRegistrationRepository clientRegistrationRepository) {
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }

    /**
     * If you want in-memory for quick dev/testing, use:
     *
     * @Bean
     * public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository repo) {
     *     return new InMemoryOAuth2AuthorizedClientService(repo);
     * }
     *
     */
}
