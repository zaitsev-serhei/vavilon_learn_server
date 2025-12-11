package com.language_practice_server.server_demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(new Info()
        .title("Vavilon Learn Server")
        .version("0.0.1")
        .description("API for Vavilon Learn server")
        .contact(new Contact().name("Oleksii Hubanov & Zaitsev Serhii").email("vavilonLearn@gmail.com")));
    }
}
