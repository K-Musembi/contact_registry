package com.backend.backend.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter intercepts requests, extracts JWT tokens, validates them,
 * and sets up Spring Security's authentication context.
 */
@Component
public class JWTFilter {

    /**
     * Defines a bean for the JWT authentication filter.
     * @param jwtService         The service for JWT token operations.
     * @param userDetailsService The service for loading user-specific data.
     * @return An instance of OncePerRequestFilter for JWT authentication.
     */
    @Bean
    public OncePerRequestFilter jwtFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    @NonNull HttpServletRequest request,
                    @NonNull HttpServletResponse response,
                    @NonNull FilterChain filterChain) throws ServletException, IOException {

                final String authHeader = request.getHeader("Authorization");
                final String jwtToken;
                String username = null;

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                jwtToken = authHeader.substring(7); // "Bearer ".length()
                try {
                    username = jwtService.extractUsername(jwtToken);
                } catch (Exception e) {
                    System.out.println("Error extracting username from JWT token:");
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // Credentials (password) are not needed for token-based auth
                                userDetails.getAuthorities()); // User roles/permissions
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
