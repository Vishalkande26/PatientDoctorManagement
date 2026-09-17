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

        return configuration
                .getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        http

            .csrf(csrf ->
                    csrf.disable()
            )

            .cors(cors -> {})

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authenticationProvider(
                    authenticationProvider
            )

            .authorizeHttpRequests(auth -> auth

                /*
                 * OPTIONS
                 */
                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                ).permitAll()

                /*
                 * SWAGGER
                 */
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()

                /*
                 * AUTH
                 */
                .requestMatchers(
                        "/api/auth/**"
                ).permitAll()

                /*
                 * USERS
                 */
                .requestMatchers(
                        "/api/users/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * DOCTORS - GET
                 */
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/doctors/**"
                ).hasAnyAuthority(
                        "ROLE_PATIENT",
                        "ROLE_DOCTOR",
                        "ROLE_ADMIN"
                )

                /*
                 * DOCTORS - CREATE
                 */
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/doctors/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * DOCTORS - UPDATE
                 */
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/doctors/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * DOCTORS - DELETE
                 */
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/doctors/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * PATIENTS - GET
                 */
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/patients/**"
                ).hasAnyAuthority(
                        "ROLE_DOCTOR",
                        "ROLE_ADMIN"
                )

                /*
                 * PATIENTS - CREATE
                 */
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/patients/**"
                ).hasAnyAuthority(
                        "ROLE_PATIENT",
                        "ROLE_ADMIN"
                )

                /*
                 * PATIENTS - UPDATE
                 */
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/patients/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * PATIENTS - DELETE
                 */
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/patients/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * =====================================
                 * APPOINTMENTS
                 * =====================================
                 */

                /*
                 * VIEW APPOINTMENTS
                 */
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/**"
                ).hasAnyAuthority(
                        "ROLE_PATIENT",
                        "ROLE_DOCTOR",
                        "ROLE_ADMIN"
                )

                /*
                 * CREATE APPOINTMENT
                 *
                 * PATIENT IS ALLOWED
                 */
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/appointments/**"
                ).hasAnyAuthority(
                        "ROLE_PATIENT",
                        "ROLE_ADMIN"
                )

                /*
                 * UPDATE APPOINTMENT
                 */
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/appointments/**"
                ).hasAnyAuthority(
                        "ROLE_DOCTOR",
                        "ROLE_ADMIN"
                )

                /*
                 * DELETE APPOINTMENT
                 */
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/appointments/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                /*
                 * DOCTOR-PATIENT
                 */
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/doctor-patient/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/doctor-patient/**"
                ).hasAuthority(
                        "ROLE_ADMIN"
                )

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/doctor-patient/**"
                ).hasAnyAuthority(
                        "ROLE_PATIENT",
                        "ROLE_DOCTOR",
                        "ROLE_ADMIN"
                )

                /*
                 * EVERYTHING ELSE
                 */
                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}