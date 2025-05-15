package com.backend.backend.person;

import com.backend.backend.person.dto.GenderStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    List<Person> findByCountyName(String countyName);

    /**
     * Retrieves contacts details of five last registered persons
     * @return List of contacts
     */
    List<Person> findTop5ByOrderByCreatedAtDesc();

    /**
     * Retrieves gender statistics of persons in contact registry
     * @return GenderStats object
     */
    @Query("SELECT new com.backend.backend.person.dto.GenderStats(" +
            "SUM(CASE WHEN LOWER(p.gender) = 'male' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN LOWER(p.gender) = 'female' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN LOWER(p.gender) = 'not specified' THEN 1 ELSE 0 END) " +
            ") FROM Person p")
    GenderStats findGenderStats();
}
