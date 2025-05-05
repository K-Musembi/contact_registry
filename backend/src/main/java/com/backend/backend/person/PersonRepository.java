package com.backend.backend.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Person entity
 * JpaRepository interface provides standard JPA CRUD operations
 * Custom query methods defined
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Custom query methods
    Optional<Person> findByEmail(String email);
    Optional<Person> findByPhone(String phone);
    List<Person> findAll();
}
