package com.se114p12.backend.seeders;

import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryInitializer {
  private final ProductCategoryRepository productCategoryRepository;
  private final JsonLoader jsonLoader;

  public void initializeCategories() {
    try {
      List<ProductCategory> categories = jsonLoader.loadCategoriesFromJson();
      System.out.println("Loaded categories: " + categories.size());
    } catch (Exception e) {
      throw new RuntimeException("Failed to load categories from JSON", e);
    }
  }
}
