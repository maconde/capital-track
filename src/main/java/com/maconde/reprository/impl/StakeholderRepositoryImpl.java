package com.maconde.reprository.impl;

import com.maconde.model.Stakeholder;
import com.maconde.reprository.StakeholderRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class StakeholderRepositoryImpl implements StakeholderRepository {
    private final Map<Long, Stakeholder> stakeholders = new HashMap<>();

    @Override
    public void save(Stakeholder stakeholder) {
        stakeholders.put(stakeholder.getId(), stakeholder);
    }

    @Override
    public Optional<Stakeholder> findById(Long id) {
        return Optional.ofNullable(stakeholders.get(id));
    }
}
