package com.backend.backend.county;

import com.backend.backend.county.dto.CountyRequest;
import com.backend.backend.county.dto.CountyResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business Logic for County entity
 * Defines logic for incoming requests and outgoing responses through the controller
 *
 * @see com.backend.backend.county.CountyRepository
 */
@Service
public class CountyService {

    private final CountyRepository countyRepository;

    @Autowired
    public CountyService(CountyRepository countyRepository) {
        this.countyRepository = countyRepository;
    }

    @Transactional
    public CountyResponse createCounty(CountyRequest countyRequest) {
        if (countyRepository.findByCode(countyRequest.code()).isPresent()) {
            throw new DataIntegrityViolationException("County code already exists");
        }
        County county = new County();
        county.setName(countyRequest.name());
        county.setCode(countyRequest.code());

        County createdCounty = countyRepository.save(county);
        return mapToCountyResponse(createdCounty);
    }

    @Transactional
    public CountyResponse updateCounty(Long Id, CountyRequest countyRequest) {
        County county = countyRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("County not found"));

        county.setName(countyRequest.name());
        county.setCode(countyRequest.code());

        County updatedCounty = countyRepository.save(county);
        return mapToCountyResponse(updatedCounty);
    }

    @Transactional
    public CountyResponse findById(Long id) {
        County county = countyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("County not found"));

        return mapToCountyResponse(county);
    }

    @Transactional
    public CountyResponse findByName(String name) {
        County county = countyRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("County not found"));

        return mapToCountyResponse(county);
    }

    @Transactional
    public CountyResponse findByCode(int code) {
        County county = countyRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("County not found"));

        return mapToCountyResponse(county);
    }

    @Transactional
    public List<CountyResponse> findAllCounties() {
        List<County> counties = countyRepository.findAll();
        return counties.stream()
                .map(this::mapToCountyResponse)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        if (countyRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("County not found");
        }

        countyRepository.deleteById(id);
    }

    private CountyResponse mapToCountyResponse(County county) {
        return new CountyResponse(
                county.getId(),
                county.getName(),
                county.getCode()
        );
    }
}
