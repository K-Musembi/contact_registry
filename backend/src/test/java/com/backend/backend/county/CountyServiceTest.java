package com.backend.backend.county;

import com.backend.backend.county.dto.CountyRequest;
import com.backend.backend.county.dto.CountyResponse;
import com.backend.backend.person.Person;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountyServiceTest {

    @Mock
    private CountyRepository countyRepository;

    @InjectMocks
    private CountyService countyService;

    private County county1;
    private County county2;
    private CountyRequest countyRequest1;
    // private CountyResponse countyResponse1;

    @BeforeEach
    void setup() {
        countyRequest1 = new CountyRequest("County Test", 123);

        county1 = new County(1L, "County One", 101, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        county2 = new County(2L, "County Two", 102, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        Person p1 = new Person(); p1.setId(10L);
        Person p2 = new Person(); p2.setId(11L);
        county1.setPersons(Arrays.asList(p1, p2));
    }

    @Test
    @DisplayName("Should return all counties")
    void shouldReturnAllCounties() {
        given(countyRepository.findAll()).willReturn(Arrays.asList(county1, county2));

        List<CountyResponse> counties = countyService.findAllCounties();

        assertThat(counties).isNotNull();
        assertThat(counties).hasSize(2);
        assertThat(counties.get(0).name()).isEqualTo(county1.getName());
        assertThat(counties.get(0).personCount()).isEqualTo(county1.getPersons().size());
        verify(countyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find county by ID when found")
    void findCountyById_whenFound() {
        given(countyRepository.findById(1L)).willReturn(Optional.of(county1));

        CountyResponse foundCounty = countyService.findById(1L);

        assertThat(foundCounty).isNotNull();
        assertThat(foundCounty.name()).isEqualTo(county1.getName());
        assertThat(foundCounty.id()).isEqualTo(county1.getId());
        assertThat(foundCounty.personCount()).isEqualTo(county1.getPersons().size());
        verify(countyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when county by ID not found")
    void findCountyById_whenNotFound() {
        given(countyRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> countyService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("County not found");
        verify(countyRepository).findById(99L);
    }

    @Test
    @DisplayName("Should find county by name when found")
    void findCountyByName_whenFound() {
        given(countyRepository.findByName("County One")).willReturn(Optional.of(county1));

        CountyResponse foundCounty = countyService.findByName("County One");

        assertThat(foundCounty).isNotNull();
        assertThat(foundCounty.personCount()).isEqualTo(county1.getPersons().size());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when county by code not found")
    void findCountyByCode_whenNotFound() {
        given(countyRepository.findByCode(anyInt())).willReturn(Optional.empty());

        assertThatThrownBy(() -> countyService.findByCode(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("County not found");
        verify(countyRepository).findByCode(999);
    }


    @Test
    @DisplayName("Should create and return new county")
    void createCounty() {
        County countyToSave = new County();
        countyToSave.setName(countyRequest1.name());
        countyToSave.setCode(countyRequest1.code());

        County savedCounty = new County(1L, countyRequest1.name(), countyRequest1.code(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());

        given(countyRepository.findByCode(countyRequest1.code())).willReturn(Optional.empty());
        given(countyRepository.save(any(County.class))).willReturn(savedCounty);

        CountyResponse createdCounty = countyService.createCounty(countyRequest1);

        assertThat(createdCounty).isNotNull();
        assertThat(createdCounty.name()).isEqualTo(countyRequest1.name());
        assertThat(createdCounty.id()).isEqualTo(savedCounty.getId());
        assertThat(createdCounty.personCount()).isEqualTo(0); // New county has 0 persons
        verify(countyRepository).save(argThat(c -> c.getName().equals(countyRequest1.name()) && c.getCode() == countyRequest1.code()));
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when creating county with existing code")
    void createCounty_whenCodeExists() {
        given(countyRepository.findByCode(countyRequest1.code())).willReturn(Optional.of(county1));

        assertThatThrownBy(() -> countyService.createCounty(countyRequest1))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("County code already exists");
        verify(countyRepository, never()).save(any(County.class));
    }


    @Test
    @DisplayName("Should update county when found")
    void updateCounty_whenFound() {
        CountyRequest updateRequest = new CountyRequest("Updated Name", 124);
        Long countyId = 1L;

        County existingCounty = new County(countyId, "Old Name", 101, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        County updatedCountyEntity = new County(countyId, updateRequest.name(), updateRequest.code(), new ArrayList<>(), existingCounty.getCreatedAt(), LocalDateTime.now());


        given(countyRepository.findById(countyId)).willReturn(Optional.of(existingCounty));
        given(countyRepository.save(any(County.class))).willReturn(updatedCountyEntity);

        CountyResponse updatedCountyResponse = countyService.updateCounty(countyId, updateRequest);

        assertThat(updatedCountyResponse).isNotNull();
        assertThat(updatedCountyResponse.name()).isEqualTo(updateRequest.name());
        assertThat(updatedCountyResponse.id()).isEqualTo(countyId);
        assertThat(updatedCountyResponse.personCount()).isEqualTo(existingCounty.getPersons().size());


        verify(countyRepository).findById(countyId);
        verify(countyRepository).save(argThat(c ->
                c.getId().equals(countyId) &&
                        c.getName().equals(updateRequest.name()) &&
                        c.getCode() == updateRequest.code()
        ));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent county")
    void updateCounty_whenNotFound() {
        CountyRequest updateRequest = new CountyRequest("Updated Name", 124);
        Long countyId = 99L;

        given(countyRepository.findById(countyId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> countyService.updateCounty(countyId, updateRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("County not found");

        verify(countyRepository).findById(countyId);
        verify(countyRepository, never()).save(any(County.class));
    }

    @Test
    @DisplayName("Should delete county when found")
    void deleteCounty_whenFound() {
        Long countyId = 1L;
        given(countyRepository.findById(countyId)).willReturn(Optional.of(county1));
        doNothing().when(countyRepository).deleteById(countyId);

        countyService.deleteById(countyId);

        verify(countyRepository, times(1)).findById(countyId);
        verify(countyRepository, times(1)).deleteById(countyId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent county")
    void deleteCounty_whenNotFound() {
        Long countyId = 99L;
        given(countyRepository.findById(countyId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> countyService.deleteById(countyId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("County not found");

        verify(countyRepository, times(1)).findById(countyId);
        verify(countyRepository, never()).deleteById(countyId);
    }
}
