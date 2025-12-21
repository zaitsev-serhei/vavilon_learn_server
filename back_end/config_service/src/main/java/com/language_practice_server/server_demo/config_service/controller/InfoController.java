package com.language_practice_server.server_demo.config_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class InfoController {

    @GetMapping("/api/info")
    public Map<String, String> info() {
        return Map.of("service", "config-service", "status", "ok");
    }
}