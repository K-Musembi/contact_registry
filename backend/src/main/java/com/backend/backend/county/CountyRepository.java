package com.backend.backend.county;

import com.backend.backend.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository
 * JpaRepository defines standard JPA CRUD operations
 * Custom query methods are defined below
 */
@Repository
public interface CountyRepository extends JpaRepository<County, Long> {

    // Custom query methods
    Optional<County> findByName(String name);
    Optional<County> findByCode(int code);
}
