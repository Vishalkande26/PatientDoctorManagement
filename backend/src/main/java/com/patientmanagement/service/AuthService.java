package com.patientmanagement.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patientmanagement.dto.LoginRequestDTO;
import com.patientmanagement.dto.LoginResponseDTO;
import com.patientmanagement.dto.RegisterRequestDTO;
import com.patientmanagement.entity.Role;
import com.patientmanagement.entity.User;
import com.patientmanagement.repository.UserRepository;
import com.patientmanagement.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    
    public String register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        
        user.setRole(Role.PATIENT);

        userRepository.save(user);

        return "User registered successfully";
    }

    
    public LoginResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}