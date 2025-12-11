package com.language_practice_server.server_demo.web.security.config;

import org.springframework.context.annotation.Configuration;

/**
 * Application-level configuration class.
 * Defines beans like UserDetailsService, PasswordEncoder, and AuthenticationManager used in Security setup.
 */

@Configuration
public class ApplicationConfiguration {
//    private final UserRepositoryJpa userRepositoryJpa; //let`s use UserRepository domain level instead
//
//    public ApplicationConfiguration(UserRepositoryJpa userRepositoryJpa) {
//        this.userRepositoryJpa = userRepositoryJpa;
//    }
//
//    @Bean
//    UserDetailsService userDetailsService() {
//        return username -> userRepositoryJpa.findByUserName(username)
//                .map(CustomUserDetails::new) //lambda-expression, or ".map(userEntity -> new CustomUserDetails(userEntity))"
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//
//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
}
