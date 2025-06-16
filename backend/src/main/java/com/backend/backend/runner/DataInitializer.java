package com.backend.backend.runner;

import com.backend.backend.county.County;
import com.backend.backend.county.CountyRepository;
import com.backend.backend.person.Person;
import com.backend.backend.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CountyRepository countyRepository;
    private final PersonRepository personRepository;

    @Override
    public void run(String... args) {
        if (countyRepository.count() == 0) {
            List<County> countiesData = List.of( // Renamed to avoid conflict
                    new County(null, "Nairobi", 47, new ArrayList<>(), null, null),
                    new County(null, "Machakos", 16, new ArrayList<>(), null, null),
                    new County(null, "Makueni", 17, new ArrayList<>(), null, null),
                    new County(null, "Mombasa", 1, new ArrayList<>(), null, null),
                    new County(null, "Laikipia", 31, new ArrayList<>(), null, null),
                    new County(null, "Nakuru", 32, new ArrayList<>(), null, null),
                    new County(null, "Nyamira", 46, new ArrayList<>(), null, null),
                    new County(null, "Busia", 40, new ArrayList<>(), null, null)
            );
            countyRepository.saveAll(countiesData);
        }

        if (personRepository.count() == 0) {
            County Nairobi = countyRepository.findByName("Nairobi")
                    .orElseThrow(() -> new RuntimeException("County Nairobi not found"));
            County Machakos = countyRepository.findByName("Machakos")
                    .orElseThrow(() -> new RuntimeException("County Machakos not found"));
            County Makueni = countyRepository.findByName("Makueni")
                    .orElseThrow(() -> new RuntimeException("County Makueni not found"));
            County Mombasa = countyRepository.findByName("Mombasa")
                    .orElseThrow(() -> new RuntimeException("County Mombasa not found"));
            County Laikipia = countyRepository.findByName("Laikipia")
                    .orElseThrow(() -> new RuntimeException("County Laikipia not found"));
            County Nakuru = countyRepository.findByName("Nakuru")
                    .orElseThrow(() -> new RuntimeException("County Nakuru not found"));
            County Nyamira = countyRepository.findByName("Nyamira")
                    .orElseThrow(() -> new RuntimeException("County Nyamira not found"));
            County Busia = countyRepository.findByName("Busia")
                    .orElseThrow(() -> new RuntimeException("County Busia not found"));

            List<Person> persons = List.of(
                    new Person(null, "Jane M.", "janem@mail.com", "0700000001", "female", LocalDate.of(2000, 1, 19), Machakos, null, null),
                    new Person(null, "Alice H.", "aliceh@mail.com", "0700000090", "female", LocalDate.of(1998, 10, 29), Machakos, null, null),
                    new Person(null, "Eve L.", "evel@mail.com", "0700000013", "female", LocalDate.of(2000, 9, 10), Machakos, null, null),
                    new Person(null, "Peter N", "petern@mail.com", "0700000009", "male", LocalDate.of(1995, 2, 18), Machakos, null, null),
                    new Person(null, "Martin K.", "martink@mail.com", "0700000028", "male", LocalDate.of(1998, 3, 15), Machakos, null, null),
                    new Person(null, "John P.", "johnp@mail.com", "0700000032", "male", LocalDate.of(2002, 7, 9), Nairobi, null, null),
                    new Person(null, "Bob S.", "bobs@mail.com", "0700000045", "male", LocalDate.of(1997, 6, 12), Nairobi, null, null),
                    new Person(null, "Carol T.", "carolt@mail.com", "0700000101", "female", LocalDate.of(1999, 5, 20), Nairobi, null, null),
                    new Person(null, "David U.", "davidu@mail.com", "0700000102", "male", LocalDate.of(2001, 8, 15), Nairobi, null, null),
                    new Person(null, "Fiona V.", "fionav@mail.com", "0700000103", "female", LocalDate.of(1996, 3, 22), Laikipia, null, null),
                    new Person(null, "George W.", "georgew@mail.com", "0700000104", "male", LocalDate.of(2003, 11, 5), Laikipia, null, null),
                    new Person(null, "Henry P.", "henryp@mail.com", "0700000105", "male", LocalDate.of(1997, 7, 1), Laikipia, null, null),
                    new Person(null, "Cathy K.", "cathyk@mail.com", "0700000106", "female", LocalDate.of(2000, 4, 10), Laikipia, null, null),
                    new Person(null, "Julius W.", "juliusw@mail.com", "0700000107", "male", LocalDate.of(2004, 1, 30), Laikipia, null, null),
                    new Person(null, "Karen A.", "karena@mail.com", "0700000108", "female", LocalDate.of(1995, 9, 3), Laikipia, null, null),
                    new Person(null, "Leo B.", "leob@mail.com", "0700000109", "male", LocalDate.of(1998, 12, 12), Nyamira, null, null),
                    new Person(null, "Mona C.", "monac@mail.com", "0700000110", "female", LocalDate.of(2002, 2, 25), Nyamira, null, null),
                    new Person(null, "Noel D.", "noeld@mail.com", "0700000111", "male", LocalDate.of(1999, 6, 8), Nyamira, null, null),
                    new Person(null, "Olivia E.", "oliviae@mail.com", "0700000112", "female", LocalDate.of(2001, 10, 17), Mombasa, null, null),
                    new Person(null, "Paul F.", "paulf@mail.com", "0700000113", "male", LocalDate.of(1996, 4, 23), Mombasa, null, null),
                    new Person(null, "Janet G.", "jannetg@mail.com", "0700000114", "female", LocalDate.of(2000, 8, 7), Makueni, null, null),
                    new Person(null, "Roy H.", "royh@mail.com", "0700000115", "male", LocalDate.of(1997, 1, 14), Makueni, null, null),
                    new Person(null, "Sara I.", "sarai@mail.com", "0700000116", "female", LocalDate.of(2003, 5, 26), Makueni, null, null),
                    new Person(null, "Tom J.", "tomj@mail.com", "0700000117", "male", LocalDate.of(1995, 11, 19), Makueni, null, null),
                    new Person(null, "Pauline K.", "paulinek@mail.com", "0700000118", "female", LocalDate.of(2004, 3, 2), Makueni, null, null),
                    new Person(null, "Victor L.", "victorl@mail.com", "0700000119", "male", LocalDate.of(1998, 7, 28), Busia, null, null),
                    new Person(null, "Wendy M.", "wendym@mail.com", "0700000120", "female", LocalDate.of(2002, 9, 13), Busia, null, null),
                    new Person(null, "Dennis N.", "dennisn@mail.com", "0700000121", "male", LocalDate.of(1999, 2, 6), Busia, null, null),
                    new Person(null, "Leah O.", "leaho@mail.com", "0700000122", "female", LocalDate.of(2001, 6, 21), Busia, null, null),
                    new Person(null, "Zacharia P.", "zachariap@mail.com", "0700000123", "male", LocalDate.of(1996, 12, 30), Busia, null, null),
                    new Person(null, "Anne O.", "anneo@mail.com", "0700000124", "female", LocalDate.of(2003, 4, 5), Busia, null, null),
                    new Person(null, "Magdalene W.", "magdalene@mail.com", "0700000125", "female", LocalDate.of(1997, 10, 11), Nakuru, null, null),
                    new Person(null, "William B.", "williamb@mail.com", "0700000126", "male", LocalDate.of(2000, 5, 16), Nakuru, null, null)
            );
            personRepository.saveAll(persons);
        }
    }
}