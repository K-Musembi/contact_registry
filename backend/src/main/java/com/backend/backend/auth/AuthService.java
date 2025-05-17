package com.backend.backend.auth;

import com.backend.backend.config.security.jwt.JWTService;
import com.backend.backend.user.User;
import com.backend.backend.user.UserRepository;
import com.backend.backend.auth.dto.AuthRequest;
import com.backend.backend.auth.dto.AuthResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

/**
 * Business logic for authentication service
 * Uses AuthenticationManager to authenticate user
 * Uses generateToken() method from JWTService to generate JWT token
 * @see com.backend.backend.config.security.SecurityConfig
 * @see JWTService
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers new user and generates token
     * @param request AuthRequest DTO
     * @return AuthResponse DTO
     */
    public AuthResponse registerUser(AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    /**
     * Authenticates user and generates token
     * AuthenticationManager is an interface; ite defers to AuthenticationProvider declared in SecurityConfig
     * @param request AuthRequest DTO
     * @return AuthResponse DTO
     */
    public AuthResponse authenticateUser(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
