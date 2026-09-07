package com.patientmanagement.dto;

import com.patientmanagement.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateRequestDTO {

    @Size(
        min = 3,
        max = 50,
        message = "Username must be between 3 and 50 characters"
    )
    private String username;

    @Email(message = "Please enter a valid email address")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$",
        message = "Email must be in valid format, for example abc@gmail.com"
    )
    private String email;

    @Size(
        min = 6,
        max = 100,
        message = "Password must be between 6 and 100 characters"
    )
    private String password;

    private Role role;

    public UserUpdateRequestDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}