package com.maconde.controller;

import com.maconde.dto.StakeholderParticipationDto;
import com.maconde.enums.StakeholderParticipationFilter;
import com.maconde.exception.CompanyNotFoundException;
import com.maconde.service.OwnershipService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnershipController.class)
class OwnershipControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnershipService ownershipService;

    @Test
    @DisplayName("given beneficiaries when getBeneficiaries then return 200 and correct data")
    void givenBeneficiaries_whenGetBeneficiaries_thenReturn200AndCorrectData() throws Exception {
        Long companyId = 1L;
        StakeholderParticipationFilter filter = StakeholderParticipationFilter.ALL;
        var participation = StakeholderParticipationDto.builder()
                .id(10L)
                .stakeholderName("Alice")
                .percentage(75.0)
                .build();

        List<StakeholderParticipationDto> beneficiaries = List.of(participation);

        when(ownershipService.calculateStakeholderParticipationsFiltered(companyId, filter)).thenReturn(beneficiaries);

        mockMvc.perform(get("/ownership/beneficiaries")
                        .param("companyId", companyId.toString())
                        .param("filter", filter.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].stakeholderName").value("Alice"))
                .andExpect(jsonPath("$[0].percentage").value(75.0));
    }

    @Test
    @DisplayName("given no beneficiaries when getBeneficiaries then return  204 (No Content)")
    void givenNoBeneficiaries_whenGetBeneficiaries_thenReturn204() throws Exception {
        Long companyId = 2L;
        StakeholderParticipationFilter filter = StakeholderParticipationFilter.PERSON;

        when(ownershipService.calculateStakeholderParticipationsFiltered(companyId, filter)).thenReturn(List.of());

        mockMvc.perform(get("/ownership/beneficiaries")
                        .param("companyId", companyId.toString())
                        .param("filter", filter.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("given company not found when getBeneficiaries then return 404")
    void givenCompanyNotFound_whenGetBeneficiaries_thenReturn404() throws Exception {
        Long companyId = 99L;
        StakeholderParticipationFilter filter = StakeholderParticipationFilter.EFFECTIVE;

        doThrow(new CompanyNotFoundException("Company not found"))
                .when(ownershipService).calculateStakeholderParticipationsFiltered(companyId, filter);

        mockMvc.perform(get("/ownership/beneficiaries")
                        .param("companyId", companyId.toString())
                        .param("filter", filter.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}