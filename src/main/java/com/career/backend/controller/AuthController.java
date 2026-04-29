package com.career.backend.controller;

import com.career.backend.dto.AuthDto;
import com.career.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDto.RegisterRequest request) {
        try {
            AuthDto.AuthResponse response = authService.register(request);
            // The frontend expects { user: newUser } but also token is needed usually.
            // Based on api.js: resolve({ data: { user: newUser } });
            Map<String, Object> body = new HashMap<>();
            body.put("user", response.getUser());
            body.put("token", response.getToken());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        try {
            AuthDto.AuthResponse response = authService.login(request);
            // Based on api.js: resolve({ data: { user } });
            Map<String, Object> body = new HashMap<>();
            body.put("user", response.getUser());
            body.put("token", response.getToken());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }
    }
}
