package com.example.internal_management_system.security.controller;

import com.example.internal_management_system.security.dto.LoginRequest;
import com.example.internal_management_system.security.dto.LoginResponse;
import com.example.internal_management_system.security.dto.RegisterRequest;
import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
