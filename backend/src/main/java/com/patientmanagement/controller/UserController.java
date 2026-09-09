package com.patientmanagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patientmanagement.dto.UserUpdateRequestDTO;
import com.patientmanagement.entity.User;
import com.patientmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @Valid @RequestBody User request) {

        Map<String, Object> response =
                userService.createUser(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO request) {

        return ResponseEntity.ok(
                userService.updateUser(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}