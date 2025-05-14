package com.backend.backend.person;

import com.backend.backend.person.dto.PersonRequest;
import com.backend.backend.person.dto.PersonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private final PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonRequest validPersonRequest;
    private PersonRequest validPersonRequest2;
    // private PersonRequest invalidPersonRequest;
    private PersonResponse personResponse1;
    private PersonResponse personResponse2;

    public PersonControllerTest(PersonService personService) {
        this.personService = personService;
    }

    @BeforeEach
    void setup() {
        validPersonRequest = new PersonRequest(
                "test", "test@gmail.com", "0700000001", "male", LocalDate.of(2000, 1, 1), "countyTest");
        validPersonRequest2 = new PersonRequest(
                "test2", "test2@gmail.com", "0700000002", "female", LocalDate.of(2000, 1, 2), "countyTest2");
        // invalidPersonRequest = new PersonRequest("", "");
        personResponse1 = new PersonResponse(
                1L, "test", "test@gmail.com", "0700000001", "male", LocalDate.of(2000, 1, 1).toString(), "countyTest");
        personResponse2 = new PersonResponse(
                2L, "test2", "test2@gmail.com", "0700000002", "female", LocalDate.of(2000, 1, 2).toString(), "countyTest2");
    }

    @Test
    @DisplayName("GET /api/v1/persons/{id} - Should return person if found")
    void getPersonById() throws Exception {
        Long personId = 1L;

        given(personService.findPersonById(personId)).willReturn(personResponse1);

        mockMvc.perform(get("/api/v1/persons/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(personResponse1.id()))
                .andExpect(jsonPath("$.name").value(personResponse1.fullName()))
                .andExpect(jsonPath("$.email").value(personResponse1.email()));

        verify(personService).findPersonById(personId);
    }

    @Test
    @DisplayName("GET /api/v1/persons/{id} - Should return 404 if person not found")
    void notFoundResponse() throws Exception {
        Long personId = 99L;
        given(personService.findPersonById(personId)).willThrow(new EntityNotFoundException("Person not found"));

        mockMvc.perform(get("/api/v1/persons/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(personService).findPersonById(personId);
    }

    @Test
    @DisplayName("GET /api/v1/persons - Should return all persons")
    void getAllPersons() throws Exception {
        given(personService.findAllPersons()).willReturn(List.of(personResponse1, personResponse2));

        mockMvc.perform(get("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(personResponse1.fullName()))
                .andExpect(jsonPath("$[1].name").value(personResponse2.fullName()))
                .andExpect(jsonPath("$.length()").value(2));

        verify(personService).findAllPersons();
    }

    @Test
    @DisplayName("POST /api/v1/persons - Should create and return new person")
    void createPerson() throws Exception {
        given(personService.createPerson(validPersonRequest)).willReturn(personResponse1);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPersonRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(personResponse1.id()))
                .andExpect(jsonPath("$.name").value(personResponse1.fullName()));

        verify(personService).createPerson(validPersonRequest);
    }

    @Test
    @DisplayName("PUT /api/v1/persons/{id} - Should update and return updated person")
    void updatePerson() throws Exception {
        Long personId = 1L;
        given(personService.updatePerson(personId, validPersonRequest2)).willReturn(personResponse2);

        mockMvc.perform(put("/api/v1/persons/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPersonRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(personResponse2.id()))
                .andExpect(jsonPath("$.name").value(personResponse2.fullName()));

        verify(personService).updatePerson(personId, validPersonRequest2);
    }

    @Test
    @DisplayName("DELETE /api/v1/persons/{id} - Should return 204 if person successfully deleted")
    void deletePerson() throws Exception {
        Long personId = 1L;

        mockMvc.perform(delete("/api/v1/persons/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(personService).deleteById(personId);
    }
}
