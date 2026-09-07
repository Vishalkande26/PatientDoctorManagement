package com.patientmanagement.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.patientmanagement.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "MyPatientManagementSystemSecretKey123456789"
            .getBytes()
    );

    private final long jwtExpiration = 1000 * 60 * 60;

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("username", user.getUsername())
                .issuedAt(new Date())
                .expiration(
                    new Date(System.currentTimeMillis() + jwtExpiration)
                )
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    public boolean isTokenValid(String token) {

        try {
            extractAllClaims(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}