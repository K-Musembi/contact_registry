package com.backend.backend.person.dto;

/**
 * Immutable record class
 * Separates internal model representation from outgoing data
 */
public record GenderStats(
        long maleCount,
        long femaleCount,
        long notSpecifiedCount
) {}
