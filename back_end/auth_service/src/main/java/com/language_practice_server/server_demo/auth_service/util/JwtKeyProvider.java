package com.language_practice_server.server_demo.auth_service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtKeyProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtKeyProvider.class);
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private String keyId;

    public JwtKeyProvider(
            @Value("${security.jwt.private-key-path}") Resource privateKeyRes,
            @Value("${security.jwt.public-key-path}") Resource publicKeyRes,
            @Value("${security.jwt.key-id}") String keyId) {
        logger.debug("Creating JwtKeyProvider with privateKey path: {} and publicKey path: {}", privateKeyRes, publicKeyRes);
        this.privateKey = loadPrivateKey(privateKeyRes);
        this.publicKey = loadPublicKey(publicKeyRes);
        this.keyId = keyId;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public String getKeyId() {
        return keyId;
    }

    private RSAPrivateKey loadPrivateKey(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            byte[] keyBytes = cleanPem(is.readAllBytes());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Failed to load RSA private key", e);
        }
    }

    private RSAPublicKey loadPublicKey(Resource resource){
        try (InputStream is = resource.getInputStream()) {
            byte[] keyBytes = cleanPem(is.readAllBytes());
            X509EncodedKeySpec spec = new X509EncodedKeySpec (keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Failed to load RSA public key", e);
        }
    }

    private byte[] cleanPem(byte[] pem) {
        String pemContent = new String(pem, StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("-----BEGIN [A-Z]+ KEY-----([\\s\\S]+?)-----END [A-Z]+ KEY-----",Pattern.MULTILINE);
        Matcher matcher =pattern.matcher(pemContent);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No private key found in PEM");
        }
        String base64 = matcher.group(1).replaceAll("\\s+", "");
        return Base64.getDecoder().decode(base64);
    }
}
