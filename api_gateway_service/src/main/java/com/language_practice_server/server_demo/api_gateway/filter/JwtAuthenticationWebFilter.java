package com.language_practice_server.server_demo.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class JwtAuthenticationWebFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationWebFilter.class);
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkUrl;
    private static final String BEARER_PREFIX = "Bearer ";
    private final ReactiveJwtDecoder decoder;

    public JwtAuthenticationWebFilter(ReactiveJwtDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(auth==null||!auth.startsWith(BEARER_PREFIX)){
            logger.debug("Authorization header is missing for exchange: {}",exchange.getRequest());
            return chain.filter(exchange);
        }
        String accessToken = auth.substring(BEARER_PREFIX.length());

        return decoder.decode(accessToken)
                .map(jwt -> {
                    Collection<SimpleGrantedAuthority> authorities =extractAuthorities(jwt);
                    AbstractAuthenticationToken token = new JwtAuthenticationToken(jwt,authorities);
                    exchange.getAttributes().put("jwt",token);
                    return token;
                })
                .flatMap(authToken->{
                  return chain.filter(exchange).contextWrite(ctx-> ReactiveSecurityContextHolder.withAuthentication(authToken));
                })
                .onErrorResume(ex->{
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt){
        Object roles = jwt.getClaim("roles");
        if(roles instanceof List){
            List<?> list = (List<?>) roles;
            return list.stream()
                    .map(Object::toString)
                    .map(r->r.startsWith("ROLE_")?r:"ROLE_"+r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        //fallback
        Object scope = jwt.getClaim("scope");
        if(scope instanceof String){
            return List.of(new SimpleGrantedAuthority("SCOPE_"+scope));
        }
        return List.of();
    }
}
