package com.patientmanagement.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ==============================
        // PUBLIC ENDPOINTS
        // ==============================

        if (path.equals("/api/auth/login")
                || path.equals("/api/auth/register")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/swagger-ui/")
                || path.equals("/v3/api-docs")
                || path.startsWith("/v3/api-docs/")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ==============================
        // GET AUTHORIZATION HEADER
        // ==============================

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            logger.debug(
                    "No Bearer token provided for request: {} {}",
                    request.getMethod(),
                    path
            );

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            // Extract email from JWT
            String email =
                    jwtService.extractEmail(token);

            if (email != null
                    && SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService
                            .loadUserByUsername(email);

                // Validate JWT
                if (jwtService.isTokenValid(token)) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                    );

                    SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                            authentication
                        );

                    logger.info(
                            "JWT authentication successful for user: {}",
                            email
                    );

                } else {

                    logger.warn(
                            "Invalid JWT token for request: {} {}",
                            request.getMethod(),
                            path
                    );
                }
            }

        } catch (Exception e) {

            logger.warn(
                    "JWT authentication failed for request: {} {} - {}",
                    request.getMethod(),
                    path,
                    e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}