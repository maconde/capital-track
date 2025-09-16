package com.maconde.reprository;

import com.maconde.model.Ownership;
import com.maconde.model.Stakeholder;

import java.util.List;

public interface OwnershipRepository {
    List<Ownership> findByTarget(Stakeholder target);
}
