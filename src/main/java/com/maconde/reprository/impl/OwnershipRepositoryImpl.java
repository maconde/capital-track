package com.maconde.reprository.impl;

import com.maconde.model.Ownership;
import com.maconde.model.Stakeholder;
import com.maconde.reprository.OwnershipRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OwnershipRepositoryImpl implements OwnershipRepository {

    @Override
    public List<Ownership> findByTarget(Stakeholder target) {
        return List.of();
    }
}
