package com.patientmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        http

            // Disable CSRF because we are using JWT
            .csrf(csrf ->
                    csrf.disable()
            )

            // Use CORS configuration from CorsConfig.java
            .cors(cors -> {})

            // JWT authentication is stateless
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authenticationProvider(
                    authenticationProvider
            )

            .authorizeHttpRequests(auth -> auth

                // Allow CORS preflight requests
                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                ).permitAll()

                // Authentication APIs
                .requestMatchers(
                        "/api/auth/**"
                ).permitAll()

                // USER APIs - ADMIN only
                .requestMatchers(
                        "/api/users/**"
                ).hasRole("ADMIN")

                // =========================
                // DOCTOR APIs
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/doctors/**"
                ).hasAnyRole(
                        "PATIENT",
                        "DOCTOR",
                        "ADMIN"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/doctors/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/doctors/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/doctors/**"
                ).hasRole("ADMIN")

                // =========================
                // PATIENT APIs
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/patients/**"
                ).hasAnyRole(
                        "DOCTOR",
                        "ADMIN"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/patients/**"
                ).hasAnyRole(
                        "PATIENT",
                        "ADMIN"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/patients/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/patients/**"
                ).hasRole("ADMIN")

                // =========================
                // APPOINTMENT APIs
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/**"
                ).hasAnyRole(
                        "PATIENT",
                        "DOCTOR",
                        "ADMIN"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/appointments/**"
                ).hasAnyRole(
                        "PATIENT",
                        "ADMIN"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/appointments/**"
                ).hasRole("DOCTOR")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/appointments/**"
                ).hasRole("ADMIN")

                // Everything else requires login
                .anyRequest()
                .authenticated()
            )

            // JWT filter
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}