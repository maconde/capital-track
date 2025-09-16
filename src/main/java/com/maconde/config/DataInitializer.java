package com.maconde.config;


import com.maconde.model.CompanyStakeholder;
import com.maconde.model.Ownership;
import com.maconde.model.PersonStakeholder;
import com.maconde.reprository.OwnershipRepository;
import com.maconde.reprository.StakeholderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final StakeholderRepository stakeholderRepository;
    private final OwnershipRepository ownershipRepository;

    public DataInitializer(StakeholderRepository stakeholderRepository, OwnershipRepository ownershipRepository) {
        this.stakeholderRepository = stakeholderRepository;
        this.ownershipRepository = ownershipRepository;
    }

    @PostConstruct
    public void init() {
        // Création des individus
        var mmeYvette = new PersonStakeholder(1L, "Mme Yvette");
        var mmeZoe = new PersonStakeholder(2L, "Mme Zoé");
        var mXavier = new PersonStakeholder(3L, "M. Xavier");
        var mYves = new PersonStakeholder(4L, "M. Yves");

        // Création des sociétés
        var societeA = new CompanyStakeholder(10L, "Société A");
        var societeB = new CompanyStakeholder(11L, "Société B");
        var societeC = new CompanyStakeholder(12L, "Société C");
        var societeD = new CompanyStakeholder(13L, "Société D");

        // Enregistrement des stakeholders
        stakeholderRepository.save(mmeYvette);
        stakeholderRepository.save(mmeZoe);
        stakeholderRepository.save(mXavier);
        stakeholderRepository.save(mYves);
        stakeholderRepository.save(societeA);
        stakeholderRepository.save(societeB);
        stakeholderRepository.save(societeC);
        stakeholderRepository.save(societeD);

        // Liens de détention
        ownershipRepository.save(new Ownership(societeB, societeA, 60.0));
        ownershipRepository.save(new Ownership(mmeZoe, societeA, 10.0));
        ownershipRepository.save(new Ownership(mXavier, societeA, 30.0));
        ownershipRepository.save(new Ownership(societeC, societeB, 50.0));
        ownershipRepository.save(new Ownership(mmeZoe, societeB, 50.0));
        ownershipRepository.save(new Ownership(mmeYvette, societeC, 90.0));
        ownershipRepository.save(new Ownership(mYves, societeC, 5.0));
        ownershipRepository.save(new Ownership(societeD, societeC, 5.0));
    }

}
