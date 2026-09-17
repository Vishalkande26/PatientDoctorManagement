package com.patientmanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patientmanagement.dto.LoginRequestDTO;
import com.patientmanagement.dto.LoginResponseDTO;
import com.patientmanagement.dto.RegisterRequestDTO;
import com.patientmanagement.entity.Patient;
import com.patientmanagement.entity.Role;
import com.patientmanagement.entity.User;
import com.patientmanagement.repository.PatientRepository;
import com.patientmanagement.repository.UserRepository;
import com.patientmanagement.security.JwtService;

@Service
public class AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;

        this.patientRepository = patientRepository;

        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;

        this.authenticationManager = authenticationManager;
    }

    public String register(RegisterRequestDTO request) {

        logger.info(
                "Registration request received for email: {}",
                request.getEmail()
        );

        if (userRepository.existsByEmail(request.getEmail())) {

            logger.warn(
                    "Registration failed. Email already registered: {}",
                    request.getEmail()
            );

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

        logger.info(
                "User registered successfully with email: {} and role: {}",
                user.getEmail(),
                user.getRole()
        );

        return "User registered successfully";
    }

    public LoginResponseDTO login(
            LoginRequestDTO request) {

        logger.info(
                "Login attempt received for email: {}",
                request.getEmail()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {

                    logger.warn(
                            "Login failed. User not found with email: {}",
                            request.getEmail()
                    );

                    return new RuntimeException(
                            "User not found"
                    );
                });

        String token =
                jwtService.generateToken(user);

        Long patientId = null;

        if (user.getRole() == Role.PATIENT) {

            Patient patient = patientRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() -> {

                        logger.warn(
                                "Patient record not found for user ID: {}",
                                user.getId()
                        );

                        return new RuntimeException(
                                "Patient record not found for this user"
                        );
                    });

            patientId = patient.getId();

            logger.info(
                    "Patient ID {} found for user ID {}",
                    patientId,
                    user.getId()
            );
        }

        logger.info(
                "User logged in successfully with email: {} and role: {}",
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                patientId
        );
    }
}