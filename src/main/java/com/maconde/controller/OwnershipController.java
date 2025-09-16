package com.maconde.controller;

import com.maconde.dto.StakeholderParticipationDto;
import com.maconde.enums.StakeholderParticipationFilter;
import com.maconde.service.OwnershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ownership")
public class OwnershipController {
    private final OwnershipService ownershipService;

    public OwnershipController(OwnershipService ownershipService) {
        this.ownershipService = ownershipService;
    }

    @GetMapping("/beneficiaries")
    public ResponseEntity<List<StakeholderParticipationDto>> getBeneficiaries(
            @RequestParam Long companyId,
            @RequestParam StakeholderParticipationFilter filter) {

        List<StakeholderParticipationDto>  stakeholdersParticipation = ownershipService.calculateStakeholderParticipationsFiltered(companyId, filter);
        if (stakeholdersParticipation.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stakeholdersParticipation);
    }
}
