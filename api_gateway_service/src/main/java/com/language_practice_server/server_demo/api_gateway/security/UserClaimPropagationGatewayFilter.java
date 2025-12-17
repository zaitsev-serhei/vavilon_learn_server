package com.language_practice_server.server_demo.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UserClaimPropagationGatewayFilter  extends AbstractGatewayFilterFactory<UserClaimPropagationGatewayFilter.Config> {

    public UserClaimPropagationGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) ->
            exchange.getPrincipal()
                    .ofType(JwtAuthenticationToken.class)
                    .flatMap(auth -> {
                        Jwt jwt = auth.getToken();
                        if (!"USER".equals(jwt.getClaimAsString("type"))) {
                            return chain.filter(exchange);
                        }
                        ServerHttpRequest mutated = exchange.getRequest().mutate()
                                .header("X-User-Id", jwt.getClaimAsString("uid"))
                                .header("X-User-Roles", jwt.getClaimAsString("roles"))
                                .build();
                        return chain.filter(exchange.mutate().request(mutated).build());
                    })
                    .switchIfEmpty(chain.filter(exchange));
    }

    public static class Config{}
}
