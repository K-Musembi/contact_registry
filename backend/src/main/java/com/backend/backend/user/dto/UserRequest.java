package com.backend.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * API request Data Transfer Object
 * @param username
 * @param password
 */
public record UserRequest(

        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 255, message = "Full name must be between 3 and 255 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
        String password,

        @NotBlank(message = "User category is required")
        @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
        String category

) {}
