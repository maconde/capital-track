package com.maconde.reprository.impl;

import com.maconde.model.Ownership;
import com.maconde.model.Stakeholder;
import com.maconde.reprository.OwnershipRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OwnershipRepositoryImpl implements OwnershipRepository {

    private final List<Ownership> ownerships = new ArrayList<>();

    @Override
    public void save(Ownership ownership) {
        ownerships.add(ownership);
    }

    @Override
    public List<Ownership> findByTarget(Stakeholder target) {
        return ownerships.stream()
                .filter(o -> o.getTarget().equals(target))
                .collect(Collectors.toList());
    }
}
