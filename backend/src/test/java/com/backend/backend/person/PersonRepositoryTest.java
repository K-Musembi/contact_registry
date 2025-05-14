package com.backend.backend.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private final PersonRepository personRepository;
    private Person person1;
    private Person person2;

    public PersonRepositoryTest(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @BeforeEach
    void setup() {
        person1 = new Person(
                1L, "person1", "test@gmail.com", "0700000001", "male", LocalDate.of(2000, 1, 1), null, null, null);
        person2 = new Person(
                2L, "person2", "test2@gmail.com", "0700000002", "female", LocalDate.of(2000, 1, 2), null, null, null);
    }

    @Test
    @DisplayName("Should save a person")
    void savePerson() {
        Person savedPerson = personRepository.save(person1);

        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isNotNull().isPositive();
        assertThat(savedPerson.getEmail()).isEqualTo(person1.getEmail());
    }

    @Test
    @DisplayName("Should find a person by Id")
    void findPersonById() {
        testEntityManager.persist(person1);
        testEntityManager.flush();

        Optional<Person> foundPerson = personRepository.findById(person1.getId());

        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getId()).isEqualTo(person1.getId());
        assertThat(foundPerson.get().getEmail()).isEqualTo(person1.getEmail());
    }

    @Test
    @DisplayName("Should find a person by email")
    void findPersonByEmail() {
        testEntityManager.persist(person1);
        testEntityManager.flush();

        Optional<Person> foundPerson = personRepository.findByEmail(person1.getEmail());

        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getId()).isEqualTo(person1.getId());
        assertThat(foundPerson.get().getPhone()).isEqualTo(person1.getPhone());
    }

    @Test
    @DisplayName("Should return empty Optional for non-existent Id")
    void emptyOptional() {
        Optional<Person> foundPerson = personRepository.findById(99L);

        assertThat(foundPerson).isEmpty();
    }

    @Test
    @DisplayName("Should find all persons")
    void findAllPersons() {
        testEntityManager.persist(person1);
        testEntityManager.persist(person2);
        testEntityManager.flush();

        List<Person> persons = personRepository.findAll();

        assertThat(persons).isNotNull();
        assertThat(persons).hasSize(2);
    }
}
