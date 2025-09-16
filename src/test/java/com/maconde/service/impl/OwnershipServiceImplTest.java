package com.maconde.service.impl;

import com.maconde.dto.StakeholderParticipationDto;
import com.maconde.enums.StakeholderParticipationFilter;
import com.maconde.enums.StakeholderType;
import com.maconde.exception.CompanyNotFoundException;
import com.maconde.model.CompanyStakeholder;
import com.maconde.model.Ownership;
import com.maconde.model.PersonStakeholder;
import com.maconde.reprository.OwnershipRepository;
import com.maconde.reprository.StakeholderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnershipServiceImplTest {
    @Mock
    private StakeholderRepository stakeholderRepository;

    @Mock
    private OwnershipRepository ownershipRepository;

    @InjectMocks
    private OwnershipServiceImpl service;

    @Test
    @DisplayName("given company does not exist when calculateStakeholderParticipations then throw CompanyNotFoundException")
    void givenCompanyDoesNotExist_whenCalculateStakeholderParticipations_thenThrowException() {
        when(stakeholderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CompanyNotFoundException.class, () -> service.calculateStakeholderParticipations(99L));
    }

    @Test
    @DisplayName("given direct owner when calculateStakeholderParticipations then return direct participation")
    void givenDirectOwner_whenCalculateStakeholderParticipations_thenReturnDirectParticipation() {

        var company = new CompanyStakeholder(1L, "Société B");
        var person = new PersonStakeholder(2L, "M. Yves");
        var ownership = Ownership.builder()
                .owner(person)
                .target(company)
                .percentage(60.0)
                .build();

        when(stakeholderRepository.findById(1L)).thenReturn(Optional.of(company));
        when(ownershipRepository.findByTarget(company)).thenReturn(List.of(ownership));
        when(ownershipRepository.findByTarget(person)).thenReturn(List.of());
        when(stakeholderRepository.findById(1L)).thenReturn(Optional.of(company));
        when(ownershipRepository.findByTarget(company)).thenReturn(List.of(ownership));
        when(ownershipRepository.findByTarget(person)).thenReturn(List.of());

        List<StakeholderParticipationDto> result = service.calculateStakeholderParticipations(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        StakeholderParticipationDto participation = result.getFirst();
        assertThat(participation.getId()).isEqualTo(2L);
        assertThat(participation.getStakeholderName()).isEqualTo("M. Yves");
        assertThat(participation.getPercentage()).isEqualTo(60.0);
        assertThat(participation.getStakeholderType()).isEqualTo(StakeholderType.PERSON);
    }

    @Test
    @DisplayName("given indirect owner when calculateStakeholderParticipations then return indirect participation")
    void givenIndirectOwner_whenCalculateStakeholderParticipations_thenReturnIndirectParticipation() {
        var companyA = new CompanyStakeholder(1L, "Société A");
        var companyB = new CompanyStakeholder(2L, "Société B");
        var person = new PersonStakeholder(3L,"M. Yves");

        var ownershipBA = Ownership.builder()
                .owner(companyB)
                .target(companyA)
                .percentage(85.0)
                .build();

        var ownershipCB = Ownership.builder()
                .owner(person)
                .target(companyB)
                .percentage(60.0)
                .build();

        when(stakeholderRepository.findById(1L)).thenReturn(Optional.of(companyA));
        when(ownershipRepository.findByTarget(companyA)).thenReturn(List.of(ownershipBA));
        when(ownershipRepository.findByTarget(companyB)).thenReturn(List.of(ownershipCB));
        when(ownershipRepository.findByTarget(person)).thenReturn(List.of());

        List<StakeholderParticipationDto> participations = service.calculateStakeholderParticipations(1L);

        StakeholderParticipationDto indirectPartcipation = participations.getFirst();


        assertThat(indirectPartcipation.getPercentage()).isEqualTo(51.0);
        assertThat(indirectPartcipation.getStakeholderName()).isEqualTo("M. Yves");
        assertThat(indirectPartcipation.getStakeholderType()).isEqualTo(StakeholderType.PERSON);
    }

    @Test
    @DisplayName("given participations exist when calculateStakeholderParticipationsFiltered with PERSON then return only persons")
    void givenParticipationsExist_whenCalculateStakeholderParticipationsFilteredWithPerson_thenReturnOnlyPersons() {
        var companyA = new CompanyStakeholder(1L, "Société A");
        var companyB = new CompanyStakeholder(2L, "Société B");
        var person = new PersonStakeholder(3L, "Mme Zoé");

        var ownershipBA = Ownership.builder()
                .owner(companyB)
                .target(companyA)
                .percentage(85.0)
                .build();

        var ownershipCB = Ownership.builder()
                .owner(person)
                .target(companyA)
                .percentage(15.0)
                .build();

        when(stakeholderRepository.findById(1L)).thenReturn(Optional.of(companyA));
        when(ownershipRepository.findByTarget(companyA)).thenReturn(List.of(ownershipBA,ownershipCB));
        when(ownershipRepository.findByTarget(person)).thenReturn(List.of());
        when(ownershipRepository.findByTarget(companyB)).thenReturn(List.of());

        List<StakeholderParticipationDto> participations = service.calculateStakeholderParticipationsFiltered(1L, StakeholderParticipationFilter.PERSON);

        assertThat(participations).isNotNull();
        assertThat(participations).hasSize(1);

        var personParticipation = participations.getFirst();

        assertThat(personParticipation.getPercentage()).isEqualTo(15.0);
        assertThat(personParticipation.getStakeholderName()).isEqualTo("Mme Zoé");
        assertThat(personParticipation.getStakeholderType()).isEqualTo(StakeholderType.PERSON);
    }


    @Test
    @DisplayName("given participations exist when calculateStakeholderParticipationsFiltered with EFFECTIVE then return only effective participations")
    void givenParticipationsExist_whenCalculateStakeholderParticipationsFilteredWithPerson_thenReturnOnlyEffectiveParticipations() {
        var companyA = new CompanyStakeholder(1L, "Société A");
        var companyB = new CompanyStakeholder(2L, "Société B");
        var personA = new PersonStakeholder(3L,"Mme Zoé");
        var personB = new PersonStakeholder(4L,"M. Yves");

        var ownershipBA = Ownership.builder()
                .owner(companyB)
                .target(companyA)
                .percentage(85.0)
                .build();

        var ownershipCB = Ownership.builder()
                .owner(personA)
                .target(companyA)
                .percentage(15.0)
                .build();

        var ownershipCD = Ownership.builder()
                .owner(personB)
                .target(companyA)
                .percentage(27.0)
                .build();

        when(stakeholderRepository.findById(1L)).thenReturn(Optional.of(companyA));
        when(ownershipRepository.findByTarget(companyA)).thenReturn(List.of(ownershipBA,ownershipCB,ownershipCD));
        when(ownershipRepository.findByTarget(personA)).thenReturn(List.of());
        when(ownershipRepository.findByTarget(personB)).thenReturn(List.of());
        when(ownershipRepository.findByTarget(companyB)).thenReturn(List.of());

        List<StakeholderParticipationDto> participations = service.calculateStakeholderParticipationsFiltered(1L, StakeholderParticipationFilter.EFFECTIVE);

        assertThat(participations).isNotNull();
        assertThat(participations).hasSize(1);

        var personParticipation = participations.getFirst();

        assertThat(personParticipation.getPercentage()).isEqualTo(27.0);
        assertThat(personParticipation.getStakeholderName()).isEqualTo("M. Yves");
        assertThat(personParticipation.getStakeholderType()).isEqualTo(StakeholderType.PERSON);
    }

    @Test
    @DisplayName("given participations exist when calculateStakeholderParticipationsFiltered with ALL then return all participations")
    void givenParticipationsExist_whenCalculateStakeholderParticipationsFilteredWithPerson_thenReturnParticipations() {
        var companyA = new CompanyStakeholder(1L, "Société A");
        var companyB = new CompanyStakeholder(2L, "Société B");
        var person = new PersonStakeholder(3L, "Mme Zoé");

        var ownershipBA = Ownership.builder()
                .owner(companyB)
                .target(companyA)
                .percentage(85.0)
                .build();

        var ownershipCB = Ownership.builder()
                .owner(person)
                .target(companyA)
                .percentage(15.0)
                .build();

        when(stakeholderRepository.findById(1L)).thenReturn(Optional.of(companyA));
        when(ownershipRepository.findByTarget(companyA)).thenReturn(List.of(ownershipBA,ownershipCB));
        when(ownershipRepository.findByTarget(person)).thenReturn(List.of());
        when(ownershipRepository.findByTarget(companyB)).thenReturn(List.of());

        List<StakeholderParticipationDto> participations = service.calculateStakeholderParticipationsFiltered(1L, StakeholderParticipationFilter.ALL);

        assertThat(participations).isNotNull();
        assertThat(participations).hasSize(2);
    }

}