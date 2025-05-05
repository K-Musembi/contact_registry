package com.backend.backend.person.dto;

/**
 * Immutable record class
 * Separates internal model representation with data sent through API
 */
public record PersonResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String gender,
        String dateOfBirth
) {}
