package com.language_practice_server.server_demo.web.security.config;

import com.language_practice_server.server_demo.web.security.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter executed once per request.
 * Extracts JWT from the Authorization header, validates it, and sets authentication in the SecurityContext.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider
            ) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Request header is missing per request: {}", request.getPathInfo());
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = authHeader.substring(7);
            logger.debug("Access token check for jwt: {}", jwt);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String userName = jwtTokenProvider.getUserNameFromJwt(jwt).orElse("unknown");
                Long userId = jwtTokenProvider.getUserIdFromJwt(jwt).orElseThrow();
                List<SimpleGrantedAuthority> authorities = jwtTokenProvider.getRolesFromJwt(jwt);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        new AuthenticatedUser(userId,userName),
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication is completed for jwt: {} --> [userId: {}, userName: {}]",jwt,userId,userName);
            }
        } catch (Exception exception) {
            logger.error("Can not set user authentication in security context");
        }
        filterChain.doFilter(request, response);
    }
    public static record AuthenticatedUser(Long userId, String userName){}
}
