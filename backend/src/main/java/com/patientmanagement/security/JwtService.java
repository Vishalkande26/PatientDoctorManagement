package com.patientmanagement.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.patientmanagement.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtService.class);

    private static final String SECRET_KEY =
            "MyPatientManagementSystemSecretKey123456789";

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

    private final long jwtExpiration =
            1000L * 60 * 60;

    /*
     * ==========================================
     * GENERATE TOKEN
     * ==========================================
     */

    public String generateToken(User user) {

        logger.info(
                "Generating JWT token for user: {} with role: {}",
                user.getEmail(),
                user.getRole()
        );

        return Jwts.builder()
                .subject(
                        user.getEmail()
                )
                .claim(
                        "role",
                        user.getRole().name()
                )
                .claim(
                        "username",
                        user.getUsername()
                )
                .issuedAt(
                        new Date()
                )
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(
                        secretKey
                )
                .compact();
    }

    /*
     * ==========================================
     * EXTRACT USERNAME / EMAIL
     * ==========================================
     */

    public String extractUsername(
            String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    /*
     * ==========================================
     * EXTRACT EMAIL
     * ==========================================
     */

    public String extractEmail(
            String token) {

        return extractUsername(token);
    }

    /*
     * ==========================================
     * EXTRACT ROLE
     * ==========================================
     */

    public String extractRole(
            String token) {

        return extractAllClaims(token)
                .get(
                        "role",
                        String.class
                );
    }

    /*
     * ==========================================
     * VALIDATE TOKEN
     * ==========================================
     */

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        try {

            String username =
                    extractUsername(token);

            return username.equals(
                    userDetails.getUsername()
            )
                    && !isTokenExpired(token);

        } catch (Exception e) {

            logger.warn(
                    "JWT validation failed: {}",
                    e.getMessage()
            );

            return false;
        }
    }

    /*
     * ==========================================
     * CHECK EXPIRATION
     * ==========================================
     */

    private boolean isTokenExpired(
            String token) {

        Date expiration =
                extractAllClaims(token)
                        .getExpiration();

        return expiration.before(
                new Date()
        );
    }

    /*
     * ==========================================
     * GET CLAIMS
     * ==========================================
     */

    private Claims extractAllClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(
                        secretKey
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}