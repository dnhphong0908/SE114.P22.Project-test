package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.neo4j.enums.RecommendationSource;
import com.se114p12.backend.neo4j.repositories.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CoPurchaseRecommendation implements RecommendationStrategy {

  private final ProductRepository productRepository;

  @Override
  public List<Long> getRecommendedProductIds(Long userId) {
    return productRepository.findRecommendedByCoPurchase(userId);
  }

  @Override
  public Double getScore() {
    return RecommendationSource.CO_PURCHASE.getScore();
  }
}
