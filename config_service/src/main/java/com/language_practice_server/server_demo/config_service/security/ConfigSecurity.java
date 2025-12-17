package com.language_practice_server.server_demo.config_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class ConfigSecurity {
//    @Bean
//    public SecurityFilterChain configServerSecurity(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/actuator/**").permitAll()
//                .anyRequest().authenticated()
//        )
//                .httpBasic(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
//}
