package com.language_practice_server.server_demo.api_gateway.auth;

import com.language_practice_server.server_demo.api_gateway.dto.ServiceTokenResponse;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class AuthWebClientConfig {
    private final WebClient webClient;

    public AuthWebClientConfig(WebClient.Builder builder,
                               @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<ServiceTokenResponse> requestServiceToken(String gatewayServiceToken){
        return webClient.post().uri("/internal/auth/service-token")
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + gatewayServiceToken)
                .retrieve()
                .bodyToMono(ServiceTokenResponse.class);
    }
}
