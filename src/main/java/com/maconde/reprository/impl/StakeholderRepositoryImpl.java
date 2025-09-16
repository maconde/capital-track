package com.maconde.reprository.impl;

import com.maconde.model.Stakeholder;
import com.maconde.reprository.StakeholderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StakeholderRepositoryImpl implements StakeholderRepository {
    @Override
    public Optional<Stakeholder> findById(Long id) {
        return Optional.empty();
    }
}
