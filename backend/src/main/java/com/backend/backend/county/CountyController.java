package com.backend.backend.county;

import com.backend.backend.county.dto.CountyRequest;
import com.backend.backend.county.dto.CountyResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller layer for County entity
 * Handles incoming and outgoing HTTP RestAPI requests
 * Makes use of ResponseEntity HTTP object
 */
@RestController
@RequestMapping("/api/v1/counties")
public class CountyController {

    private final CountyService countyService;

    @Autowired
    public CountyController(CountyService countyService) {
        this.countyService = countyService;
    }

    @GetMapping
    public ResponseEntity<List<CountyResponse>> getAllCounties() {
        List<CountyResponse> responseObject = countyService.findAllCounties();

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountyResponse> getCountyById(@PathVariable Long id) {
        CountyResponse responseObject = countyService.findById(id);

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CountyResponse> getCountyByName(@PathVariable String name) {
        CountyResponse responseObject = countyService.findByName(name);

        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CountyResponse> getCountyByCode(@PathVariable int code) {
        CountyResponse responseObject = countyService.findByCode(code);

        return ResponseEntity.ok(responseObject);
    }

    @PostMapping
    public ResponseEntity<CountyResponse> createCounty(
            @Valid @RequestBody CountyRequest countyRequest) {
        CountyResponse responseObject = countyService.createCounty(countyRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountyResponse> updateCounty(
            @PathVariable Long id,
            @Valid @RequestBody CountyRequest countyRequest) {
        CountyResponse responseObject = countyService.updateCounty(id, countyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounty(@PathVariable Long id) {
        countyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
