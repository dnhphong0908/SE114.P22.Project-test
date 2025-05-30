package com.se114p12.backend.repositories.variation;

import com.se114p12.backend.entities.variation.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VariationOptionRepository extends JpaRepository<VariationOption, Long>,
        JpaSpecificationExecutor<VariationOption> {
    boolean existsByValueAndVariationId(String value, Long id);
    List<VariationOption> findByVariationId(Long variationId);
}
