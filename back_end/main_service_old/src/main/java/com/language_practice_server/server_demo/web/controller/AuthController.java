package com.language_practice_server.server_demo.web.controller;

import com.language_practice_server.server_demo.domain.model.User;
import com.language_practice_server.server_demo.mapper.UserWebMapper;
import com.language_practice_server.server_demo.service.RefreshTokenService;
import com.language_practice_server.server_demo.service.UserService;
import com.language_practice_server.server_demo.web.dto.AuthResponse;
import com.language_practice_server.server_demo.web.dto.UserDto;
import com.language_practice_server.server_demo.web.security.service.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService tokenService;
    private final UserService userService;
    private final UserWebMapper userMapper;
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

    public AuthController(JwtTokenProvider jwtTokenProvider, RefreshTokenService tokenService, UserService userService, UserWebMapper userMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenService = tokenService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Fetch user info after authorization"
            , description = "Once authorized front-end calls this end-point to fetch data from the server. " +
            "Also provides JWT access and secured refresh tokens to be set in front-end.")
    @ApiResponse(responseCode = "201", description = "AuthResponse provided")
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            logger.debug("Performing me() for jwt: {}", jwt);
            if (jwtTokenProvider.validateToken(jwt)) {
                Optional<User> savedUser = findUserWhenValidJWT(jwt);
                if (savedUser.isPresent()) {
                    UserDto userDto = userMapper.toDto(savedUser.get());
                    AuthResponse authResponse = new AuthResponse(userDto, jwt);
                    logger.debug("Successful me(). Responding with userDto: {}", userDto);
                    return ResponseEntity.ok(authResponse);
                }
            }
        }
        Cookie cookie = WebUtils.getCookie(request, cookieKey);
        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            String plainRefreshToken = cookie.getValue();
            Optional<User> savedUser = tokenService.validateTokenAndGetUserByRefreshToken(plainRefreshToken);
            if (savedUser.isPresent()) {
                logger.info("Refresh token is valid. Setting up secured cookie in me() for userId: {}", savedUser.get().getId());
                return getAuthResponseEntityWithCookie(response, savedUser.get(), plainRefreshToken);
            } else {
                savedUser = tokenService.checkExpiredAndGetUserToRotate(plainRefreshToken);
                if (savedUser.isPresent()) {
                    logger.info("Refresh token is expired for userId: {} and will be rotated.", savedUser.get().getId());
                    String serverRefreshToken = tokenService.rotateRefreshToken(plainRefreshToken, savedUser.get().getId());
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
    public ResponseEntity<RefreshResponseDto> refresh(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      @RequestHeader(value = "Authorization", required = false) String authorization) {
        //if refresh method was call before expiration of JWT token
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String jwt = authorization.substring(7);
            logger.debug("Performing refresh() for jwt: {}", jwt);
            if (jwtTokenProvider.validateToken(jwt)) {
                Optional<User> savedUser = findUserWhenValidJWT(jwt);
                if (savedUser.isPresent()) {
                    String accessToken = jwtTokenProvider.createAccessToken(savedUser.get());
                    logger.debug("Successful refresh(). New access token created: {}", accessToken);
                    return ResponseEntity.ok(new RefreshResponseDto(accessToken));
                }
            }
        }
        //if we are here Access token is not valid. Check refresh token
        Cookie cookie = WebUtils.getCookie(request, cookieKey);
        if (cookie != null && StringUtils.hasText(cookie.getValue())) {
            String plainRefreshToken = cookie.getValue();
            Optional<User> savedUser = tokenService.validateTokenAndGetUserByRefreshToken(plainRefreshToken);
            if (savedUser.isPresent()) {
                logger.info("Refresh token is valid. Setting up secured cookie in me() for userId: {}", savedUser.get().getId());
                return getRefreshResponseEntityWithCookie(response, savedUser.get(), plainRefreshToken);
            } else {
                savedUser = tokenService.checkExpiredAndGetUserToRotate(plainRefreshToken);
                if (savedUser.isPresent()) {
                    logger.info("Refresh token is expired for userId: {} and will be rotated.", savedUser.get().getId());
                    String serverRefreshToken = tokenService.rotateRefreshToken(plainRefreshToken, savedUser.get().getId());
                    return getRefreshResponseEntityWithCookie(response, savedUser.get(), serverRefreshToken);
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
            tokenService.revokeRefreshToken(token);
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
                                                                         User savedUser,
                                                                         String serverRefreshToken) {
        ResponseCookie responseCookie = ResponseCookie.from(cookieKey, serverRefreshToken)
                .httpOnly(httpOnly)
                .secure(isSecure)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(sameSite)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        String accessToken = jwtTokenProvider.createAccessToken(savedUser);
        UserDto userDto = userMapper.toDto(savedUser);
        AuthResponse authResponse = new AuthResponse(userDto, accessToken);
        logger.info("server response: {}", authResponse.getAccessToken());
        return ResponseEntity.ok(authResponse);
    }

    private ResponseEntity<RefreshResponseDto> getRefreshResponseEntityWithCookie(HttpServletResponse response,
                                                                                  User savedUser,
                                                                                  String serverRefreshToken) {
        ResponseCookie responseCookie = ResponseCookie.from(cookieKey, serverRefreshToken)
                .httpOnly(httpOnly)
                .secure(isSecure)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(sameSite)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        String accessToken = jwtTokenProvider.createAccessToken(savedUser);
        RefreshResponseDto refreshResponse = new RefreshResponseDto(accessToken);
        logger.info("Refresh token is used. Server response: {}", refreshResponse.accessToken());
        return ResponseEntity.ok(refreshResponse);
    }

    private Optional<User> findUserWhenValidJWT(String jwt) {
        if (jwtTokenProvider.getUserNameFromJwt(jwt).isEmpty() && jwtTokenProvider.getUserIdFromJwt(jwt).isEmpty()) {
            //TODO: create new Custom exception for JWT process
            logger.error("User name and id are missing from JWT token. Suspicious!");
            throw new IllegalArgumentException("Name is missing in JWT token");
        }
        Long userId = jwtTokenProvider.getUserIdFromJwt(jwt).get();
        return userService.findUserById(userId);
    }

}

record RefreshResponseDto(String accessToken) {
}
