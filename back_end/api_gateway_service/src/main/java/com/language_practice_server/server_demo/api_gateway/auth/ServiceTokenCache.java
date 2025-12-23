package com.language_practice_server.server_demo.api_gateway.auth;

import com.language_practice_server.server_demo.api_gateway.dto.ServiceTokenResponse;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;

@Component
public class ServiceTokenCache {
    private volatile String token;
    private volatile Instant expiresAt;

    public synchronized Optional<String> getValidToken(){
        if(token == null||expiresAt.isBefore(Instant.now().plusSeconds(30))){
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public synchronized void updateToken(ServiceTokenResponse response){
        this.token = response.getToken();
        this.expiresAt = Instant.now().plusSeconds(response.getExpiresAt());
    }
}
