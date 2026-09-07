package com.patientmanagement.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patientmanagement.entity.User;
import com.patientmanagement.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        
        if (request.getUsername() != null &&
                !request.getUsername().isBlank()) {

            user.setUsername(request.getUsername());
        }

        
        if (request.getEmail() != null &&
                !request.getEmail().isBlank()) {

            user.setEmail(request.getEmail());
        }

        
        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updatedUser = userRepository.save(user);

        
        return ResponseEntity.ok(
                Map.of(
                        "id", updatedUser.getId(),
                        "username", updatedUser.getUsername(),
                        "email", updatedUser.getEmail(),
                        "role", updatedUser.getRole()
                )
        );
    }
}