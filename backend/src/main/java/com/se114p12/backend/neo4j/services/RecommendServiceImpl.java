package com.se114p12.backend.neo4j.services;

import com.se114p12.backend.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendServiceImpl implements RecommendService {

  private final List<RecommendationStrategy> recommendationStrategies = new ArrayList<>();
  private final GeneralRecommendation generalRecommendation;
  private final JwtUtil jwtUtil;

  // register recommendation strategies
  @PostConstruct
  private void init(
      HistoryBaseRecommendation historyBaseRecommendation,
      CategoryBaseRecommendation categoryBaseRecommendation,
      CoPurchaseRecommendation coPurchaseRecommendation) {
    recommendationStrategies.add(historyBaseRecommendation);
    recommendationStrategies.add(categoryBaseRecommendation);
    recommendationStrategies.add(coPurchaseRecommendation);
  }

  public List<Long> getRecommendProductIds() {
    Long userId = jwtUtil.getCurrentUserId();
    if (userId == null) {
      return generalRecommendation.getRecommendedProductIds();
    }

    Map<Long, Double> productScores = new HashMap<>();
    for (RecommendationStrategy strategy : recommendationStrategies) {
      List<Long> recommendedProductIds = strategy.getRecommendedProductIds(userId);
      for (Long productId : recommendedProductIds) {
        productScores.put(
            productId, productScores.getOrDefault(productId, 0.0) + strategy.getScore());
      }
    }

    // Sort products by score in descending order
    return productScores.entrySet().stream()
        .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
        .map(Map.Entry::getKey)
        .toList();
  }
}
