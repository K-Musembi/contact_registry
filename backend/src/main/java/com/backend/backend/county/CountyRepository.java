package com.backend.backend.county;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    /**
     * Retrieve top five counties by contact details count
     * @return List of counties
     */
    @Query("SELECT c FROM County c LEFT JOIN c.persons p GROUP BY c ORDER BY COUNT(p) DESC LIMIT 5")
    List<County> findTop5ByPersonCount();
}
