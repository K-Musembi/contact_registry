package com.backend.backend.county;

import com.backend.backend.county.dto.CountyRequest;
import com.backend.backend.county.dto.CountyResponse;
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

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountyController.class)
public class CountyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountyService countyService;

    @Autowired
    private ObjectMapper objectMapper;

    private CountyRequest validCountyRequest;
    private CountyResponse countyResponse1;
    private CountyResponse countyResponse2;

    @BeforeEach
    void setup() {
        validCountyRequest = new CountyRequest("Valid County", 101);

        countyResponse1 = new CountyResponse(1L, "County One", 5);
        countyResponse2 = new CountyResponse(2L, "County Two", 10);
    }

    @Test
    @DisplayName("GET /api/v1/counties - Should return all counties")
    void getAllCounties() throws Exception {
        given(countyService.findAllCounties()).willReturn(Arrays.asList(countyResponse1, countyResponse2));

        mockMvc.perform(get("/api/v1/counties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(countyResponse1.name()))
                .andExpect(jsonPath("$[0].personCount").value(countyResponse1.personCount()))
                .andExpect(jsonPath("$[1].name").value(countyResponse2.name()))
                .andExpect(jsonPath("$.length()").value(2));

        verify(countyService).findAllCounties();
    }

    @Test
    @DisplayName("GET /api/v1/counties/{id} - Should return county if found")
    void getCountyById_ifFound() throws Exception {
        Long countyId = 1L;
        given(countyService.findById(countyId)).willReturn(countyResponse1);

        mockMvc.perform(get("/api/v1/counties/{id}", countyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(countyResponse1.id()))
                .andExpect(jsonPath("$.name").value(countyResponse1.name()))
                .andExpect(jsonPath("$.personCount").value(countyResponse1.personCount()));

        verify(countyService).findById(countyId);
    }

    @Test
    @DisplayName("GET /api/v1/counties/{id} - Should return 404 if county not found")
    void getCountyById_ifNotFound() throws Exception {
        Long countyId = 99L;
        given(countyService.findById(countyId)).willThrow(new EntityNotFoundException("County not found"));

        mockMvc.perform(get("/api/v1/counties/{id}", countyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(countyService).findById(countyId);
    }

    @Test
    @DisplayName("GET /api/v1/counties/name/{name} - Should return county if found")
    void getCountyByName_ifFound() throws Exception {
        String countyName = "County One";
        given(countyService.findByName(countyName)).willReturn(countyResponse1);

        mockMvc.perform(get("/api/v1/counties/name/{name}", countyName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(countyResponse1.id()))
                .andExpect(jsonPath("$.personCount").value(countyResponse1.personCount()));}


    @Test
    @DisplayName("GET /api/v1/counties/code/{code} - Should return 404 if county not found")
    void getCountyByCode_ifNotFound() throws Exception {
        int countyCode = 999;
        given(countyService.findByCode(countyCode)).willThrow(new EntityNotFoundException("County not found"));

        mockMvc.perform(get("/api/v1/counties/code/{code}", countyCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(countyService).findByCode(countyCode);
    }

    @Test
    @DisplayName("GET /api/v1/counties/top-counties - Should return top five counties")
    void getTopFiveCounties() throws Exception {
        given(countyService.findTopFiveCounties()).willReturn(Collections.singletonList(countyResponse1));

        mockMvc.perform(get("/api/v1/counties/top-counties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(countyResponse1.name()))
                .andExpect(jsonPath("$.length()").value(1));
        verify(countyService).findTopFiveCounties();
    }


    @Test
    @DisplayName("POST /api/v1/counties - Should create and return new county")
    void createCounty() throws Exception {
        given(countyService.createCounty(any(CountyRequest.class))).willReturn(countyResponse1);

        mockMvc.perform(post("/api/v1/counties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCountyRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(countyResponse1.id()))
                .andExpect(jsonPath("$.name").value(countyResponse1.name()));

        verify(countyService).createCounty(any(CountyRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/counties/{id} - Should update and return updated county")
    void updateCounty() throws Exception {
        Long countyId = 1L;
        CountyRequest updateRequest = new CountyRequest("Updated County", 102);
        CountyResponse updatedResponse = new CountyResponse(countyId, "Updated County", 7);

        given(countyService.updateCounty(eq(countyId), any(CountyRequest.class))).willReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/counties/{id}", countyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isCreated()) // update returns 201
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedResponse.id()))
                .andExpect(jsonPath("$.name").value(updatedResponse.name()));

        verify(countyService).updateCounty(eq(countyId), any(CountyRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/counties/{id} - Should return 404 if county to update not found")
    void updateCounty_ifNotFound() throws Exception {
        Long countyId = 99L;
        CountyRequest updateRequest = new CountyRequest("Updated County", 102);

        given(countyService.updateCounty(eq(countyId), any(CountyRequest.class)))
                .willThrow(new EntityNotFoundException("County not found"));

        mockMvc.perform(put("/api/v1/counties/{id}", countyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(countyService).updateCounty(eq(countyId), any(CountyRequest.class));
    }


    @Test
    @DisplayName("DELETE /api/v1/counties/{id} - Should return 204 if county successfully deleted")
    void deleteCounty() throws Exception {
        Long countyId = 1L;
        doNothing().when(countyService).deleteById(countyId);

        mockMvc.perform(delete("/api/v1/counties/{id}", countyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(countyService).deleteById(countyId);
    }

    @Test
    @DisplayName("DELETE /api/v1/counties/{id} - Should return 404 if county to delete not found")
    void deleteCounty_ifNotFound() throws Exception {
        Long countyId = 99L;
        doThrow(new EntityNotFoundException("County not found")).when(countyService).deleteById(countyId);

        mockMvc.perform(delete("/api/v1/counties/{id}", countyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(countyService).deleteById(countyId);
    }
}