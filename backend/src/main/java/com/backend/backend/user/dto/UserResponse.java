package com.backend.backend.user.dto;

/**
 * API response Data Transfer Object
 * @param Id
 * @param username
 * @param category
 */
public record UserResponse(
        Long Id,
        String username,
        String category  // admin or regular
) {}
