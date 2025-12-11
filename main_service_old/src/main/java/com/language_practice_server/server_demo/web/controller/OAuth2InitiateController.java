package com.language_practice_server.server_demo.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class OAuth2InitiateController {
    private static final String SESSION_REDIRECT_URI_KEY = "oauth2_redirect_uri";
    private static final String SESSION_REDIRECT_URI_GOOGLE = "/oauth2/authorization/google";
    @Value("${app.frontend.default-success-redirect}")
    private String defaultFrontendCallback;

    @GetMapping("/api/oauth2/authorize/google")
    public void authorizeGoogle(
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // If provider returned an error (e.g. user denied consent), forward to frontend callback with error info
        if (error != null) {
            String saved = (request.getSession(false) != null) ? (String) request.getSession(false).getAttribute(SESSION_REDIRECT_URI_KEY) : null;
            String frontendTarget = (saved != null && !saved.isBlank()) ? saved : (defaultFrontendCallback != null ? defaultFrontendCallback : "/");
            String encodedError = URLEncoder.encode(error, StandardCharsets.UTF_8);
            String redirectToFrontend = frontendTarget + (frontendTarget.contains("?") ? "&" : "?") + "oauth_error=" + encodedError;

            // clean up session key
            if (request.getSession(false) != null) request.getSession().removeAttribute(SESSION_REDIRECT_URI_KEY);

            response.sendRedirect(redirectToFrontend);
            return;
        }
        if (redirectUrl == null || redirectUrl.isBlank()) {
            throw new IllegalArgumentException("Redirect url is missing");
        }

        // Normal flow: store redirectUrl in session if provided
        if (redirectUrl != null && !redirectUrl.isBlank()) {
            request.getSession(true).setAttribute(SESSION_REDIRECT_URI_KEY, redirectUrl);
        }

        // If no redirectUrl provided now, we simply proceed to Spring's authorization endpoint.
        // Success handler will read saved redirect from session (or fallback to defaultFrontendCallback).
        request.getSession(true).setAttribute(SESSION_REDIRECT_URI_KEY, redirectUrl);
        response.sendRedirect(SESSION_REDIRECT_URI_GOOGLE);
    }

    /**
     * Helper for success handler to fetch and remove stored redirectUrl.
     */
    public static String consumeSavedRedirectUrl(HttpServletRequest request, String defaultFrontendCallback) {
        if (request == null) return defaultFrontendCallback;
        Object o = request.getSession(false) != null ? request.getSession(false).getAttribute(SESSION_REDIRECT_URI_KEY) : null;
        if (o != null) {
            request.getSession().removeAttribute(SESSION_REDIRECT_URI_KEY);
            return o.toString();
        }
        return defaultFrontendCallback;
    }
}

