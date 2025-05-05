package com.backend.backend.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Immutable record class
 * Separates internal model representation with data accessed externally through API
 * Uses Hibernate validator dependency for validation
 */
public record PersonRequest(
        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 characters")
        String phone,

        @NotBlank(message = "Gender is required")
        @Size(min = 3, max = 10, message = "Gender must be between 3 and 50 characters")
        String gender,

        @NotBlank(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDateTime dateOfBirth,

        @NotBlank(message = "County name is required")
        @Size(min = 3, max = 50, message = "County must be between 3 and 50 characters")
        String countyName
) {}
