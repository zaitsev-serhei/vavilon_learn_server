package com.language_practice_server.server_demo.web.security.handler;

import com.language_practice_server.server_demo.common.enums.Role;
import com.language_practice_server.server_demo.domain.model.Person;
import com.language_practice_server.server_demo.domain.model.User;
import com.language_practice_server.server_demo.service.RefreshTokenService;
import com.language_practice_server.server_demo.service.UserService;
import com.language_practice_server.server_demo.web.controller.OAuth2InitiateController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Value("${refresh.cookie.name}")
    private String cookieName;
    @Value("${refresh.cookie.http-only}")
    private boolean httpOnly;
    @Value("${refresh.cookie.secure}")
    private boolean isSecure;
    @Value("${refresh.cookie.same-site}")
    private String sameSite;
    @Value("${app.frontend.default-success-redirect}")
    private String frontRedirect;
    @Value("${refresh.cookie.max-age}")
    private Long cookieMaxAge;

    public OAuth2AuthenticationSuccessHandler(OAuth2AuthorizedClientService authorizedClientService,
                                              UserService userService,
                                              RefreshTokenService refreshTokenService) {
        this.authorizedClientService = authorizedClientService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            logger.warn("Authentication is not OAuth2AuthenticationToken: {}", authentication);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported authentication");
            return;
        }
        logger.info("Authentication with OAuth2 successful");
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(registrationId, principalName);
        if (client == null) {
            logger.warn("No authorized client found for principal {} registration {}", principalName, registrationId);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authorized client missing");
            return;
        }

        // provider token
        String providerRefreshToken = client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null;

        // extract user info from principal attributes (OpenID Connect)
        Map<String, Object> attrs = oauthToken.getPrincipal().getAttributes();
        String providerId = (String) attrs.getOrDefault("sub", attrs.get("id"));
        String email = (String) attrs.get("email");
        String name = (String) attrs.getOrDefault("name", "User");
        String firstName = null;
        String lastName = null;
        if (attrs.containsKey("given_name")) {
            firstName = (String) attrs.get("given_name");
        }
        if (attrs.containsKey("family_name")) {
            lastName = (String) attrs.get("family_name");
        }
        if ((firstName == null || lastName == null) && attrs.containsKey("name")) {
            String fullName = (String) attrs.get("name");
            if (fullName != null && fullName.trim().length() > 0) {
                String[] parts = fullName.trim().split("\\s+");
                if (parts.length > 0) firstName = parts[0];
                if (parts.length > 1) lastName = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
            }
        }
        if (firstName == null) firstName = "User";
        if (lastName == null) lastName = "User";
        logger.debug("Extracted claims for user provided by providerId:{} --> [email:{} ; name:{}; firstName:{}; lastName:{}]",
                providerId,
                email,
                name,
                firstName,
                lastName);

        String finalFirstName = firstName;
        String finalLastName = lastName;
        User user = userService.findByEmail(email).orElseGet(() -> {
            User u = new User();
            Person p = new Person();
            u.setActive(true);
            u.setEmail(email);
            u.setUserName(name);
            p.setFirstName(finalFirstName);
            p.setLastName(finalLastName);
            u.setRole(Role.TEACHER); // default role
            u.setPerson(p);
            logger.debug("User was not found in the DB. Creating new user {}", u);
            return userService.updateUser(u);
        });

        logger.info("Creating new refresh token for userId: {}", user.getId());
        String serverRefreshToken = refreshTokenService.createRefreshTokenForUser(user.getId(),
                providerRefreshToken, registrationId);
        logger.info("Creating new access token for userId: {}", user.getId());

        ResponseCookie cookie = ResponseCookie.from(cookieName, serverRefreshToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(sameSite)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        logger.info("Setting secured cookies for user: {}", user.getId());
        // redirect to frontend callback (frontend will call /api/auth/me to get user + access token)
        String redirectUrl = OAuth2InitiateController.consumeSavedRedirectUrl(request, frontRedirect);
        if (redirectUrl == null || redirectUrl.isBlank()) {
            redirectUrl = frontRedirect;
        }
        String frontendCallback = redirectUrl;
        String state = request.getParameter("state");
        String redirect = UriComponentsBuilder.fromUriString(frontendCallback)
                .queryParamIfPresent("state", java.util.Optional.ofNullable(state))
                .build()
                .toUriString();
        logger.debug("Authentication successfully completed for userId: {}. Redirect is sent to: {}",
                user.getId(),
                redirectUrl);
        response.sendRedirect(redirect);
    }
}

