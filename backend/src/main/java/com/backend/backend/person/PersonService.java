package com.backend.backend.person;

import com.backend.backend.county.County;
import com.backend.backend.county.CountyRepository;
import com.backend.backend.person.dto.GenderStats;
import com.backend.backend.person.dto.PersonRequest;
import com.backend.backend.person.dto.PersonResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Business logic for Person entity
 * Defines logic for incoming requests and outgoing responses through the controller
 * @see com.backend.backend.person.PersonRepository
 * @see com.backend.backend.person.PersonController
 */
@Service
public class PersonService {
    private final PersonRepository personRepository1;
    private final CountyRepository countyRepository;
    private final TemplateEngine templateEngine;

    @Autowired
    public PersonService(
            PersonRepository personRepository, CountyRepository countyRepository, TemplateEngine templateEngine) {
        this.countyRepository = countyRepository;
        this.personRepository1 = personRepository;
        this.templateEngine = templateEngine;
    }

    @Transactional
    public PersonResponse createPerson(PersonRequest personRequest) {
        if (personRepository1.findByEmail(personRequest.email()).isPresent()) {
            throw new DataIntegrityViolationException("Email already registered");
        }

        Person person = new Person();
        Person createdPerson = mapToPerson(person, personRequest);
        return mapToPersonResponse(createdPerson);
    }

    @Transactional
    public PersonResponse updatePerson(Long Id, PersonRequest personRequest) {
        Person person = personRepository1.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        Person createdPerson = mapToPerson(person, personRequest);
        return mapToPersonResponse(createdPerson);
    }

    @Transactional
    public List<PersonResponse> findAllPersons() {
        List<Person> persons = personRepository1.findAll();

        return persons.stream()
                .map(this::mapToPersonResponse)
                .toList();
    }

    @Transactional
    public PersonResponse findPersonById(Long Id) {
        Person person = personRepository1.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        return mapToPersonResponse(person);
    }

    @Transactional
    public PersonResponse findPersonByEmail(String email) {
        Person person = personRepository1.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        return mapToPersonResponse(person);
    }

    @Transactional
    public PersonResponse findByPhone(String phone) {
        Person person = personRepository1.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        return mapToPersonResponse(person);
    }

    @Transactional
    public List<PersonResponse> findPersonsByCountyName(String name) {
        List<Person> persons = personRepository1.findByCountyName(name);

        return persons.stream()
                .map(this::mapToPersonResponse)
                .toList();
    }

    @Transactional
    public List<PersonResponse> findLastFiveCreatedPersons() {
        List<Person> persons = personRepository1.findTop5ByOrderByCreatedAtDesc();

        return persons.stream()
                .map(this::mapToPersonResponse)
                .toList();
    }

    @Transactional
    public GenderStats findGenderStats() {
        List<Person> persons = personRepository1.findAll();
        long maleCount = 0; long femaleCount = 0; long notSpecifiedCount = 0;

        for (Person person : persons) {
            switch (person.getGender()) {
                case "Male" -> maleCount++;
                case "Female" -> femaleCount++;
                case "Not Specified" -> notSpecifiedCount++;
            }
        }
        return new GenderStats(maleCount, femaleCount, notSpecifiedCount);
        // return personRepository1.findGenderStats();
    }

    @Transactional
    public void deleteById(Long Id) {
        if (personRepository1.findById(Id).isEmpty()) {
            throw new EntityNotFoundException("Person not found");
        }
        personRepository1.deleteById(Id);
    }

    @Transactional
    public byte[] generatePDFReport(Long Id) {
        Person person = personRepository1.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        PersonResponse personDetails = mapToPersonResponse(person);

        Context context = new Context();
        context.setVariable("person", personDetails);
        String html = templateEngine.process("PersonReport.html", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }

    }

    private Person mapToPerson(Person person, PersonRequest personRequest) {
        County county = countyRepository.findByName(personRequest.countyName())
                .orElseThrow(() -> new EntityNotFoundException("County not found"));

        person.setFullName(personRequest.fullName());
        person.setEmail(personRequest.email());
        person.setPhone(personRequest.phone());
        person.setGender(personRequest.gender());
        person.setDateOfBirth(personRequest.dateOfBirth());
        person.setCounty(county);

        return personRepository1.save(person);
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFullName(),
                person.getEmail(),
                person.getPhone(),
                person.getGender(),
                person.getDateOfBirth().toString(),
                person.getCounty().getName()
        );
    }

}
