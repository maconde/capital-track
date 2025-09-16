package com.maconde.service;


import com.maconde.dto.StakeholderParticipationDto;
import com.maconde.enums.StakeholderParticipationFilter;

import java.util.List;

public interface OwnershipService {
    List<StakeholderParticipationDto> calculateStakeholderParticipations(Long companyId);
    List<StakeholderParticipationDto> calculateStakeholderParticipationsFiltered(
            Long companyId, StakeholderParticipationFilter filter);
}
