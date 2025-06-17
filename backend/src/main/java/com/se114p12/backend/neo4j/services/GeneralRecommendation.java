package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GeneralRecommendation {

  private final ProductNeo4jRepository productRepository;

  public List<Long> getRecommendedProductIds() {
    return productRepository.findGeneralRecommended();
  }

  public Double getScore() {
    return 0.0; // General recommendations have a default score
  }
}
