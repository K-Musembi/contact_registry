package com.backend.backend.county.dto;

import com.backend.backend.person.Person;

import java.util.List;

/**
 * Immutable record class
 * Separates internal model representation from outgoing data
 * Uses Hibernate validator dependency for validation
 */
public record CountyResponse(
        Long id,
        String name,
        int code,
        List<Person> persons
) {}
