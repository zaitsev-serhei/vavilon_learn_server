package com.language_practice_server.server_demo.auth_service.controller;

import com.language_practice_server.server_demo.auth_service.util.JwtKeyProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/.well-known")
public class JwksController {
    private final JwtKeyProvider keyProvider;

    public JwksController(JwtKeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @GetMapping("/jwks.json")
    public Map<String, Object> getJwks() {
        RSAPublicKey publicKey = keyProvider.getPublicKey();
        Map<String, Object> jwk = new HashMap<>();
        jwk.put("kty", "RSA");
        jwk.put("alg", "RS256");
        jwk.put("use", "sig");
        jwk.put("kid", keyProvider.getKeyId());
        jwk.put("n", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray()));
        jwk.put("e", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray()));
        Map<String, Object> response = new HashMap<>();
        response.put("keys", List.of(jwk));
        return response;
    }
}
