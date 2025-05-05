package com.backend.backend.person;

import com.backend.backend.person.dto.PersonRequest;
import com.backend.backend.person.dto.PersonResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller layer for Person Entity
 * Defines methods to handle HTTP requests and responses
 * Makes use of ResponseEntity object
 *
 * @see com.backend.backend.person.PersonService
 */
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPersons() {
        List<PersonResponse> responseObject = personService.findAllPersons();

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        PersonResponse responseObject = personService.findPersonById(id);

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonResponse> getPersonByEmail(@PathVariable String email) {
        PersonResponse responseObject = personService.findPersonByEmail(email);

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/county/{name}")
    public ResponseEntity<List<PersonResponse>> getPersonsByCounty(@PathVariable String name) {
        List<PersonResponse> responseObject = personService.findPersonsByCountyName(name);

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<PersonResponse> getPersonByPhone(@PathVariable String phone) {
        PersonResponse responseObject = personService.findByPhone(phone);

        return ResponseEntity.ok(responseObject);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(
            @Valid @RequestBody PersonRequest personRequest
            ) {
        PersonResponse responseObject = personService.createPerson(personRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(
            @PathVariable Long id,
            @Valid @RequestBody PersonRequest personRequest) {
        PersonResponse responseObject = personService.updatePerson(id, personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
