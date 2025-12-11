package com.language_practice_server.server_demo.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    //TODO: add logger here
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                //TODO: update URL when Euroka implemented
                .route("task-service", r -> r.path("/api/tasks/**")
                        .uri("http://localhost:8081")) // task-service port
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("http://localhost:8082")) // user-service port
                .build();
    }
}
