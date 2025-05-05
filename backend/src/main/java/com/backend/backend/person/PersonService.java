package com.backend.backend.person;

import com.backend.backend.person.dto.PersonRequest;
import com.backend.backend.person.dto.PersonResponse;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Business logic for Person entity
 * Defines logic for incoming requests and outgoing responses through the controller
 * @see com.backend.backend.person.PersonRepository
 * @see com.backend.backend.person.PersonController
 */
@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public PersonResponse createPerson(PersonRequest personRequest) {
        if (personRepository.findByEmail(personRequest.email()).isPresent()) {
            throw new DataIntegrityViolationException("Email already registered");
        }
        Person person = new Person();
        person.setFullName(personRequest.fullName());
        person.setEmail(personRequest.email());
        person.setPhone(personRequest.phone());
        person.setGender(personRequest.gender());
        person.setDateOfBirth(personRequest.dateOfBirth());

        Person createdPerson = personRepository.save(person);
        return mapToPersonResponse(createdPerson);
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFullName(),
                person.getEmail(),
                person.getPhone(),
                person.getGender(),
                person.getDateOfBirth().toString()
        );
    }

}
