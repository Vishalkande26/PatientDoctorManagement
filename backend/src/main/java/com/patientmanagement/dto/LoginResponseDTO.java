package com.patientmanagement.dto;

import com.patientmanagement.entity.Role;

public class LoginResponseDTO {

    private String token;
    private String username;
    private String email;
    private Role role;
    private Long patientId;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(
            String token,
            String username,
            String email,
            Role role,
            Long patientId) {

        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.patientId = patientId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}