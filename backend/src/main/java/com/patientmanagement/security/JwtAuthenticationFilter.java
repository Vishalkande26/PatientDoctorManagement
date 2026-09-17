package com.patientmanagement.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        this.jwtService =
                jwtService;

        this.userDetailsService =
                userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        /*
         * ==========================================
         * NO AUTHORIZATION HEADER
         * ==========================================
         */

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        /*
         * ==========================================
         * GET JWT
         * ==========================================
         */

        String jwt =
                authHeader.substring(7);

        try {

            /*
             * ==========================================
             * EXTRACT EMAIL
             * ==========================================
             */

            String email =
                    jwtService.extractUsername(jwt);

            /*
             * ==========================================
             * CHECK AUTHENTICATION
             * ==========================================
             */

            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                /*
                 * ==========================================
                 * LOAD USER
                 * ==========================================
                 */

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(
                                        email
                                );

                /*
                 * ==========================================
                 * VALIDATE JWT
                 * ==========================================
                 */

                if (jwtService.isTokenValid(
                        jwt,
                        userDetails
                )) {

                    /*
                     * ==========================================
                     * CREATE AUTHENTICATION
                     * ==========================================
                     */

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails
                                            .getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    /*
                     * ==========================================
                     * STORE AUTHENTICATION
                     * ==========================================
                     */

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );

                    System.out.println(
                            "JWT authentication successful"
                    );

                    System.out.println(
                            "Authenticated user: "
                                    + email
                    );

                    System.out.println(
                            "Authorities: "
                                    + userDetails
                                            .getAuthorities()
                    );
                }
            }

        } catch (Exception e) {

            SecurityContextHolder
                    .clearContext();

            System.out.println(
                    "JWT authentication failed: "
                            + e.getMessage()
            );
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}