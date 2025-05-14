package com.backend.backend.person;

import com.backend.backend.person.dto.PersonRequest;
import com.backend.backend.person.dto.PersonResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person1;
    private Person person2;
    private PersonRequest personRequest;

    @BeforeEach
    void setup() {
        personRequest = new PersonRequest(
                "test", "test@gmail.com", "0700000001", "male", LocalDate.of(2000, 1, 3), "testCounty");
        person1 = new Person(
                1L, "one", "one@gmail.com", "0700000001", "male", LocalDate.of(2000, 1, 1), null, null, null);
        person2 = new Person(
                2L, "two", "two@gmail.com", "0700000002", "female", LocalDate.of(2000, 1, 2), null, null, null);
    }

    @Test
    @DisplayName("Should return all persons")
    void shouldReturnAllPersons() {
        // Given
        given(personRepository.findAll()).willReturn(Arrays.asList(person1, person2));
        // When
        List<PersonResponse> persons = personService.findAllPersons();
        // Then
        assertThat(persons).isNotNull();
        assertThat(persons).hasSize(2);
        verify(personRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should display person by ID when found")
    void displayPersonById() {
        // Given
        given(personRepository.findById(1L)).willReturn(Optional.of(person1));
        // When
        PersonResponse foundPerson = personService.findPersonById(1L);
        // Then
        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.fullName()).isEqualTo(person1.getFullName());
        assertThat(foundPerson.email()).isEqualTo(person1.getEmail());
        assertThat(foundPerson.id()).isEqualTo(person1.getId());
        verify(personRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when person not found")
    void personNotFound() {
        // Given
        given(personRepository.findById(anyLong())).willReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> personService.findPersonById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Person not found");
        verify(personRepository).findById(99L);
    }

    @Test
    @DisplayName("Should create and return new person")
    void createPerson() {
        // Mock repository save operation with ID assigned
        Person savedPerson = new Person(
                1L,
                personRequest.fullName(),
                personRequest.email(),
                personRequest.phone(),
                personRequest.gender(),
                personRequest.dateOfBirth(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        // Given
        given(personRepository.save(any(Person.class))).willReturn(savedPerson);
        // Then
        PersonResponse createdPerson = personService.createPerson(personRequest);

        assertThat(createdPerson).isNotNull();
        assertThat(createdPerson.fullName()).isEqualTo(personRequest.fullName());
        assertThat(createdPerson.id()).isEqualTo(savedPerson.getId());
        verify(personRepository).save(argThat(p -> p.getFullName().equals(personRequest.fullName())));
    }

    @Test
    @DisplayName("Should update person when found")
    void updatePerson() {
        PersonRequest updateRequest = new PersonRequest(
                "updatedName", "updatedEmail", "updatedPhone", "updatedGender", LocalDate.now(), "updatedCounty");
        Long personId = 1L;

        // Given
        given(personRepository.findById(personId)).willReturn(Optional.of(person1));
        // When
        PersonResponse updatedPerson = personService.updatePerson(personId, updateRequest);
        // Then
        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.fullName()).isEqualTo(updateRequest.fullName());
        assertThat(updatedPerson.email()).isEqualTo(updateRequest.email());

        verify(personRepository).findById(personId);
        verify(personRepository).save(argThat(p ->
                p.getId().equals(personId) &&
                        p.getFullName().equals(updateRequest.fullName())));
    }

    @Test
    @DisplayName("Should delete person when found")
    void deletePerson() {
        Long personId = 1L;
        // Given
        given(personRepository.existsById(personId)).willReturn(true);
        // Mock delete operation
        doNothing().when(personRepository).deleteById(personId);

        personService.deleteById(personId);
        // Then
        verify(personRepository, times(1)).deleteById(personId);
    }
}
