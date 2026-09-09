package com.patientmanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patientmanagement.dto.UserUpdateRequestDTO;
import com.patientmanagement.entity.User;
import com.patientmanagement.exception.ConflictException;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Map<String, Object>> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Map<String, Object> createUser(User request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered");
        }

        if (request.getRole() == null) {
            throw new ConflictException("Role is required");
        }

        request.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(request);

        return toResponse(savedUser);
    }

    public Map<String, Object> updateUser(
            Long id,
            UserUpdateRequestDTO request) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        if (request.getUsername() != null
                && !request.getUsername().isBlank()) {

            user.setUsername(
                    request.getUsername().trim()
            );
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            String email = request.getEmail().trim();

            if (!email.equals(user.getEmail())
                    && userRepository.existsByEmail(email)) {

                throw new ConflictException(
                        "Email already registered"
                );
            }

            user.setEmail(email);
        }

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        if (request.getRole() != null) {

            user.setRole(
                    request.getRole()
            );
        }

        User updatedUser =
                userRepository.save(user);

        return toResponse(updatedUser);
    }

    public void deleteUser(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }

    private Map<String, Object> toResponse(User user) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                user.getId()
        );

        response.put(
                "username",
                user.getUsername()
        );

        response.put(
                "email",
                user.getEmail()
        );

        response.put(
                "role",
                user.getRole()
        );

        return response;
    }
}