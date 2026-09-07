package com.patientmanagement.service;

import java.util.HashMap;
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

        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;
    }


   

    public Map<String, Object> updateUser(
            Long id,
            UserUpdateRequestDTO request) {

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + id
                                )
                        );


        
        if (request.getUsername() != null
                && !request.getUsername().isBlank()) {

            user.setUsername(
                    request.getUsername()
            );
        }


        

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            if (!request.getEmail()
                    .equals(user.getEmail())
                    && userRepository
                            .existsByEmail(
                                    request.getEmail()
                            )) {

                throw new ConflictException(
                        "Email already registered"
                );
            }

            user.setEmail(
                    request.getEmail()
            );
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


        
        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                updatedUser.getId()
        );

        response.put(
                "username",
                updatedUser.getUsername()
        );

        response.put(
                "email",
                updatedUser.getEmail()
        );

        response.put(
                "role",
                updatedUser.getRole()
        );

        return response;
    }
}