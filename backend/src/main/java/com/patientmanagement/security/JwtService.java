package com.patientmanagement.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.patientmanagement.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtService.class);

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "MyPatientManagementSystemSecretKey123456789"
            .getBytes()
    );

    private final long jwtExpiration = 1000 * 60 * 60;

    // ==============================
    // GENERATE JWT
    // ==============================

    public String generateToken(User user) {

        logger.info(
                "Generating JWT token for user: {} with role: {}",
                user.getEmail(),
                user.getRole()
        );

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("username", user.getUsername())
                .issuedAt(new Date())
                .expiration(
                    new Date(
                        System.currentTimeMillis()
                            + jwtExpiration
                    )
                )
                .signWith(secretKey)
                .compact();
    }

    // ==============================
    // EXTRACT EMAIL
    // ==============================

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // ==============================
    // EXTRACT ROLE
    // ==============================

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // ==============================
    // VALIDATE JWT
    // ==============================

    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch (Exception e) {

            logger.warn(
                    "JWT validation failed: {}",
                    e.getMessage()
            );

            return false;
        }
    }

    // ==============================
    // EXTRACT ALL CLAIMS
    // ==============================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}