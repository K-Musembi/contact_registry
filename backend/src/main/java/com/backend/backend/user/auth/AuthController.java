package com.backend.backend.user.auth;

import com.backend.backend.user.auth.dto.AuthRequest;
import com.backend.backend.user.auth.dto.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP endpoints for registering new users and authenticating existing users
 * @see AuthService
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest request) {
        AuthResponse responseObject = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        AuthResponse responseObject = authService.authenticateUser(request);
        return ResponseEntity.ok().body(responseObject);
    }
}