package com.language_practice_server.server_demo.api_gateway.security;

import com.language_practice_server.server_demo.api_gateway.auth.AuthWebClientConfig;
import com.language_practice_server.server_demo.api_gateway.auth.ServiceTokenCache;
import com.language_practice_server.server_demo.api_gateway.dto.ServiceTokenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class InternalServiceTokenGatewayFilter extends AbstractGatewayFilterFactory<InternalServiceTokenGatewayFilter.Config> {
    private final AuthWebClientConfig webClient;
    private final ServiceTokenCache tokenCache;

    public InternalServiceTokenGatewayFilter(AuthWebClientConfig webClient, ServiceTokenCache tokenCache) {
        super(Config.class);
        this.webClient = webClient;
        this.tokenCache = tokenCache;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> Mono.defer(() ->
                tokenCache.getValidToken()
                .map(Mono::just)
                .orElseGet(() ->
                        webClient.requestServiceToken(getGatewayAuth(exchange))
                                .doOnNext(tokenCache::updateToken)
                                .map(ServiceTokenResponse::getToken)
                ))
                .flatMap(token -> {
                    ServerHttpRequest mutated = exchange.getRequest().mutate()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .build();
                    return chain.filter(exchange.mutate().request(mutated).build());
                });
    }

    private String getGatewayAuth(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }



    public static class Config{}
}
