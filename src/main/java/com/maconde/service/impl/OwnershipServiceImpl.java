package com.maconde.service.impl;


import com.maconde.dto.StakeholderParticipationDto;
import com.maconde.enums.StakeholderParticipationFilter;
import com.maconde.service.OwnershipService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnershipServiceImpl implements OwnershipService {

    @Override
    public List<StakeholderParticipationDto> calculateStakeholderParticipationsFiltered(Long companyId, StakeholderParticipationFilter filter) {
        return List.of();
    }
}
