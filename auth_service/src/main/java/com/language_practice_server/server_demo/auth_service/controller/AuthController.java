package com.language_practice_server.server_demo.auth_service.controller;

import com.language_practice_server.server_demo.auth_service.client.UserServiceClient;
import com.language_practice_server.server_demo.auth_service.dto.AuthResponse;
import com.language_practice_server.server_demo.auth_service.dto.RefreshResponse;
import com.language_practice_server.server_demo.auth_service.dto.UserDto;
import com.language_practice_server.server_demo.auth_service.service.RefreshTokenService;
import com.language_practice_server.server_demo.auth_service.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceClient userService;
    @Value("${refresh.cookie.name}")
    private String cookieKey;
    @Value("${refresh.cookie.http-only}")
    private boolean httpOnly;
    @Value("${refresh.cookie.secure}")
    private boolean isSecure;
    @Value("${refresh.cookie.same-site}")
    private String sameSite;
    @Value("${refresh.cookie.max-age}")
    private Long cookieMaxAge;

    public AuthController(JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService,
                          UserServiceClient userServiceClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userService = userServiceClient;
    }

    @Operation(summary = "Fetch user info after authorization"
            , description = "Once authorized front-end calls this end-point to fetch data from the server. " +
            "Also provides JWT access and secured refresh tokens to be set in front-end.")
    @ApiResponse(responseCode = "201", description = "AuthResponse provided")
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            logger.debug("Performing me() for jwt: {}", jwt);
            if (jwtTokenProvider.validateToken(jwt)) {
                Optional<UserDto> savedUser = findUserWhenValidJWT(jwt);
                if (savedUser.isPresent()) {
                    UserDto userDto = savedUser.get();
                    AuthResponse authResponse = new AuthResponse(userDto, jwt);
                    logger.debug("Successful me(). Responding with userDto: {}", userDto);
                    return ResponseEntity.ok(authResponse);
                }
            }
        }
        Cookie cookie = WebUtils.getCookie(request, cookieKey);
        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            String plainRefreshToken = cookie.getValue();
            Optional<UserDto> savedUser = refreshTokenService.validateTokenAndGetUserByRefreshToken(plainRefreshToken);
            if (savedUser.isPresent()) {
                logger.info("Refresh token is valid. Setting up secured cookie in me() for userId: {}", savedUser.get().getUserId());
                return getAuthResponseEntityWithCookie(response, savedUser.get(), plainRefreshToken);
            } else {
                savedUser = refreshTokenService.checkExpiredAndGetUserToRotate(plainRefreshToken);
                if (savedUser.isPresent()) {
                    logger.info("Refresh token is expired for userId: {} and will be rotated.", savedUser.get().getUserId());
                    String serverRefreshToken = refreshTokenService.rotateRefreshToken(plainRefreshToken, savedUser.get().getUserId());
                    return getAuthResponseEntityWithCookie(response, savedUser.get(), serverRefreshToken);
                }
            }
        }
        return ResponseEntity.status(401).build();
    }

    @Operation(summary = "Refresh Access token or revoke refresh token"
            , description = "Provides new JWT access and secured refresh tokens(if needed) to be set in front-end.")
    @ApiResponse(responseCode = "201", description = "RefreshResponseDto provided")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestHeader(value = "Authorization", required = false) String authorization) {
        //if refresh method was call before expiration of JWT token
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            logger.debug("Performing refresh() for jwt: {}", jwt);
            if (jwtTokenProvider.validateToken(jwt)) {
                Optional<UserDto> savedUser = findUserWhenValidJWT(jwt);
                if (savedUser.isPresent()) {
                    String accessToken = jwtTokenProvider.createUserAccessToken(savedUser.get().getUserId());
                    logger.debug("Successful refresh(). New access token created: {}", accessToken);
                    return ResponseEntity.ok(new RefreshResponse(accessToken));
                }
            }
        }
        //if we are here Access token is not valid. Check refresh token
        Cookie cookie = WebUtils.getCookie(request, cookieKey);
        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            String plainRefreshToken = cookie.getValue();
            Optional<UserDto> savedUser = refreshTokenService.validateTokenAndGetUserByRefreshToken(plainRefreshToken);
            if (savedUser.isPresent()) {
                logger.info("Refresh token is valid. Setting up secured cookie in me() for userId: {}", savedUser.get().getUserId());
                return getRefreshResponseEntityWithCookie(response, savedUser.get().getUserId(), plainRefreshToken);
            } else {
                savedUser = refreshTokenService.checkExpiredAndGetUserToRotate(plainRefreshToken);
                if (savedUser.isPresent()) {
                    logger.info("Refresh token is expired for userId: {} and will be rotated.", savedUser.get().getUserId());
                    String serverRefreshToken = refreshTokenService.rotateRefreshToken(plainRefreshToken, savedUser.get().getUserId());
                    return getRefreshResponseEntityWithCookie(response, savedUser.get().getUserId(), serverRefreshToken);
                }
            }
        }
        return ResponseEntity.status(401).build();
    }

    @Operation(summary = "Performs logout from the application"
            , description = "Performs logout from the application and removes refresh token from the from-end")
    @ApiResponse(responseCode = "201", description = "Redirect to login page")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, cookieKey);
        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            String token = cookie.getValue();
            refreshTokenService.revokeRefreshToken(token);
            // clear cookie on front-end. Refresh is saved in DB and can be used when next login
            ResponseCookie clear = ResponseCookie.from(cookieKey, "")
                    .httpOnly(httpOnly)
                    .secure(isSecure)
                    .path("/")
                    .maxAge(0)
                    .sameSite(sameSite)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, clear.toString());
        }
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<AuthResponse> getAuthResponseEntityWithCookie(HttpServletResponse response,
                                                                         UserDto userDto,
                                                                         String serverRefreshToken) {
        ResponseCookie responseCookie = ResponseCookie.from(cookieKey, serverRefreshToken)
                .httpOnly(httpOnly)
                .secure(isSecure)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(sameSite)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        String accessToken = jwtTokenProvider.createUserAccessToken(userDto.getUserId());
        AuthResponse authResponse = new AuthResponse(userDto, accessToken);
        logger.info("server response: {}", authResponse.getAccessToken());
        return ResponseEntity.ok(authResponse);
    }

    private ResponseEntity<RefreshResponse> getRefreshResponseEntityWithCookie(HttpServletResponse response,
                                                                               Long userId,
                                                                               String serverRefreshToken) {
        ResponseCookie responseCookie = ResponseCookie.from(cookieKey, serverRefreshToken)
                .httpOnly(httpOnly)
                .secure(isSecure)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(sameSite)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        String accessToken = jwtTokenProvider.createUserAccessToken(userId);
        RefreshResponse refreshResponse = new RefreshResponse(accessToken);
        logger.info("Refresh token is used. Server response: {}", refreshResponse.getToken());
        return ResponseEntity.ok(refreshResponse);
    }

    private Optional<UserDto> findUserWhenValidJWT(String jwt) {
        if (jwtTokenProvider.getUserNameFromJwt(jwt).isEmpty() && jwtTokenProvider.getUserIdFromJwt(jwt).isEmpty()) {
            //TODO: create new Custom exception for JWT process
            logger.error("User name and id are missing from JWT token. Suspicious!");
            throw new IllegalArgumentException("Name or Id is missing in JWT token");
        }
        Long userId = jwtTokenProvider.getUserIdFromJwt(jwt).get();
        return Optional.ofNullable(userService.findUserById(userId));
    }

}

