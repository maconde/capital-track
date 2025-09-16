package com.maconde.reprository;


import com.maconde.model.Stakeholder;

import java.util.Optional;

public interface StakeholderRepository {
    void save(Stakeholder stakeholder);
    Optional<Stakeholder> findById(Long id);
}
