package com.language_practice_server.server_demo.auth_service.controller;

import com.language_practice_server.server_demo.auth_service.dto.ServiceTokenResponse;
import com.language_practice_server.server_demo.auth_service.util.JwtTokenProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth")
@PreAuthorize("hasAuthority('SCOPE_internal')")
public class InternalAuthController {
    private final JwtTokenProvider tokenProvider;

    public InternalAuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/service-token")
    ServiceTokenResponse issueServiceToken(Authentication authentication) {
        String serviceName = authentication.getName();
        String token = tokenProvider.createServiceAccessToken(serviceName);
        return new ServiceTokenResponse(token, 3600);
    }
}
