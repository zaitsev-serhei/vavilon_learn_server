package com.language_practice_server.server_demo.auth_service.client;

import com.language_practice_server.server_demo.auth_service.dto.ServiceTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @PostMapping("/internal/auth/service-token")
    ServiceTokenResponse getServiceToken();
}
