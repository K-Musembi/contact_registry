package com.backend.backend.county.dto;

/**
 * Immutable record class
 * Separates internal model representation from outgoing data
 * Uses Hibernate validator dependency for validation
 */
public record CountyResponse(
        Long id,
        String name,
        int code
) {}
