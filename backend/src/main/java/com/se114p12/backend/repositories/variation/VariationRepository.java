package com.se114p12.backend.repositories.variation;

import com.se114p12.backend.entities.variation.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariationRepository extends JpaRepository<Variation, Long>,
        JpaSpecificationExecutor<Variation> {
    boolean existsByName(String name);
}
