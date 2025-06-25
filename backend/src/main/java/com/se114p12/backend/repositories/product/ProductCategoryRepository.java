package com.se114p12.backend.repositories.product;

import com.se114p12.backend.entities.product.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>,
        JpaSpecificationExecutor<ProductCategory> {
    boolean existsByName(@NotBlank String name);
}
