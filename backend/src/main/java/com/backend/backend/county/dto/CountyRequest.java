package com.backend.backend.county.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Immutable record class
 * Separates internal model representation from incoming data
 * Uses Hibernate validator dependency for validation
 */
public record CountyRequest(

        @NotBlank(message = "County name is required")
        @Size(min = 3, max = 50, message = "County name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "County code is required")
        @Min(message = "County code must be a positive number", value = 1)
        int code
) {}
