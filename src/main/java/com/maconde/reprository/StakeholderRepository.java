package com.maconde.reprository;


import com.maconde.model.Stakeholder;

import java.util.Optional;

public interface StakeholderRepository {
    Optional<Stakeholder> findById(Long id);
}
