package com.language_practice_server.server_demo.auth_service.web.handler;

import com.language_practice_server.server_demo.auth_service.client.AuthServiceClient;
import com.language_practice_server.server_demo.auth_service.dto.ServiceTokenResponse;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private final AuthServiceClient authServiceClient;
    private volatile String cachedToken;
    private volatile Instant expiresAt = Instant.MIN;
    public FeignClientInterceptor(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(isInternalCall(requestTemplate)){
            String token = getValidToken();
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
    }

    private boolean isInternalCall(RequestTemplate template) {
        return template.feignTarget().name().contains("user-service");
    }

    private synchronized String getValidToken(){
        if(cachedToken==null|| Instant.now().isAfter(expiresAt.minusSeconds(30))){
            ServiceTokenResponse response = authServiceClient.getServiceToken();
            cachedToken = response.getToken();
            expiresAt = Instant.now().plusSeconds(response.getExpiresAt());
        }
        return cachedToken;
    }
}
