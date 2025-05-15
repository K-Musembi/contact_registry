package com.backend.backend.county;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CountyRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CountyRepository countyRepository;

    private County county1;
    private County county2;
    // private County county3;

    @BeforeEach
    void setup() {
        county1 = new County(null, "CountyOne", 101, null, null, null);
        county2 = new County(null, "CountyTwo", 102, null, null, null);
    }

    @Test
    @DisplayName("Should save a county")
    void saveCounty() {
        County savedCounty = countyRepository.save(county1);

        assertThat(savedCounty).isNotNull();
        assertThat(savedCounty.getId()).isNotNull().isPositive();
        assertThat(savedCounty.getName()).isEqualTo(county1.getName());
        assertThat(savedCounty.getCode()).isEqualTo(county1.getCode());
    }

    @Test
    @DisplayName("Should find a county by Id")
    void findCountyById() {
        County persistedCounty1 = testEntityManager.persistFlushFind(county1);

        Optional<County> foundCounty = countyRepository.findById(persistedCounty1.getId());

        assertThat(foundCounty).isPresent();
        assertThat(foundCounty.get().getId()).isEqualTo(persistedCounty1.getId());
        assertThat(foundCounty.get().getName()).isEqualTo(persistedCounty1.getName());
    }

    @Test
    @DisplayName("Should find a county by name")
    void findCountyByName() {
        testEntityManager.persistAndFlush(county1);

        Optional<County> foundCounty = countyRepository.findByName(county1.getName());

        assertThat(foundCounty).isPresent();
        assertThat(foundCounty.get().getName()).isEqualTo(county1.getName());
        assertThat(foundCounty.get().getCode()).isEqualTo(county1.getCode());
    }

    @Test
    @DisplayName("Should find a county by code")
    void findCountyByCode() {
        testEntityManager.persistAndFlush(county1);

        Optional<County> foundCounty = countyRepository.findByCode(county1.getCode());

        assertThat(foundCounty).isPresent();
        assertThat(foundCounty.get().getCode()).isEqualTo(county1.getCode());
        assertThat(foundCounty.get().getName()).isEqualTo(county1.getName());
    }

    @Test
    @DisplayName("Should return empty Optional for non-existent Id")
    void emptyOptionalForNonExistentId() {
        Optional<County> foundCounty = countyRepository.findById(999L);
        assertThat(foundCounty).isEmpty();
    }

    @Test
    @DisplayName("Should find all counties")
    void findAllCounties() {
        testEntityManager.persist(county1);
        testEntityManager.persist(county2);
        testEntityManager.flush();

        List<County> counties = countyRepository.findAll();

        assertThat(counties).isNotNull();
        assertThat(counties).hasSize(2);
        assertThat(counties).extracting(County::getName).containsExactlyInAnyOrder(county1.getName(), county2.getName());
    }
}