package com.maconde.service.impl;


import com.maconde.dto.StakeholderParticipationDto;
import com.maconde.enums.StakeholderParticipationFilter;
import com.maconde.enums.StakeholderType;
import com.maconde.exception.CompanyNotFoundException;
import com.maconde.model.CompanyStakeholder;
import com.maconde.model.Ownership;
import com.maconde.model.PersonStakeholder;
import com.maconde.model.Stakeholder;
import com.maconde.reprository.OwnershipRepository;
import com.maconde.reprository.StakeholderRepository;
import com.maconde.service.OwnershipService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OwnershipServiceImpl implements OwnershipService {
    private final StakeholderRepository stakeholderRepository;
    private final OwnershipRepository ownershipRepository;
    private static final double INITIAL_PARTICIPATION = 1.0;
    private static final double EFFECTIVE_BENEFICIARY_THRESHOLD = 25.0;


    public OwnershipServiceImpl(StakeholderRepository stakeholderRepository, OwnershipRepository ownershipRepository){
        this.stakeholderRepository = stakeholderRepository;
        this.ownershipRepository = ownershipRepository;
    }

    @Override
    public List<StakeholderParticipationDto> calculateStakeholderParticipations(Long companyId) {

        Map<Stakeholder, Double> participations = new HashMap<>();
        Stakeholder targetCompany = stakeholderRepository.findById(companyId)
                .filter(stakeholder -> stakeholder instanceof CompanyStakeholder)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        calculateRecursive(targetCompany, INITIAL_PARTICIPATION, participations, new HashSet<>());

        return participations.entrySet().stream()
                .map(this::getStakeholderParticipation)
                .toList();
    }

    @Override
    public List<StakeholderParticipationDto> calculateStakeholderParticipationsFiltered(
            Long companyId, StakeholderParticipationFilter filter) {

        List<StakeholderParticipationDto> participations = calculateStakeholderParticipations(companyId);

        return switch (filter) {
            case ALL -> participations;
            case PERSON -> participations.stream()
                    .filter(participation -> participation.getStakeholderType() == StakeholderType.PERSON)
                    .toList();
            case EFFECTIVE -> participations.stream()
                    .filter(participation -> participation.getStakeholderType() == StakeholderType.PERSON && participation.getPercentage() >= EFFECTIVE_BENEFICIARY_THRESHOLD)
                    .toList();
        };
    }

    private void calculateRecursive(Stakeholder current, double accumulated, Map<Stakeholder, Double> participations, Set<Long> visited) {
        if (visited.contains(current.getId())) return;
        visited.add(current.getId());

        List<Ownership> owners = ownershipRepository.findByTarget(current);
        if (owners.isEmpty()) {
            participations.merge(current, accumulated * 100, Double::sum);
        } else {
            for (Ownership ownership : owners) {
                Stakeholder owner = ownership.getOwner();
                double newAccumulated = accumulated * ownership.getPercentage() / 100.0;
                calculateRecursive(owner, newAccumulated, participations, visited);
            }
        }

        visited.remove(current.getId());
    }

    //todo: move to mapper class
    private  StakeholderParticipationDto getStakeholderParticipation(Map.Entry<Stakeholder, Double> entry) {
        Stakeholder stakeholder = entry.getKey();
        var stakeholderType = (stakeholder instanceof PersonStakeholder) ? StakeholderType.PERSON : StakeholderType.COMPANY;
        double percentage = entry.getValue();
        return StakeholderParticipationDto.builder()
                .id(stakeholder.getId())
                .stakeholderName(stakeholder.getName())
                .percentage(percentage)
                .stakeholderType(stakeholderType)
                .build();
    }
}
