package com.patientmanagement.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.patientmanagement.entity.User;
import com.patientmanagement.repository.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository =
                userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        System.out.println(
                "Loading user: "
                        + email
        );

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found: "
                                                + email
                                )
                        );

        /*
         * PATIENT
         * becomes
         * ROLE_PATIENT
         */

        String authority =
                "ROLE_" +
                        user.getRole().name();

        System.out.println(
                "User: "
                        + user.getEmail()
        );

        System.out.println(
                "Role: "
                        + user.getRole()
        );

        System.out.println(
                "Authority: "
                        + authority
        );

        return org.springframework.security.core.userdetails.User
                .withUsername(
                        user.getEmail()
                )
                .password(
                        user.getPassword()
                )
                .authorities(
                        new SimpleGrantedAuthority(
                                authority
                        )
                )
                .build();
    }
}