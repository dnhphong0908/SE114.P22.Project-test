package com.se114p12.backend.controllers.stats;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.services.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(AppConstant.API_BASE_PATH + "/stats")
@RequiredArgsConstructor
@RestController
public class StatsController {
  private final StatsService statsService;

  @GetMapping("/user")
  public ResponseEntity<?> getUserStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @GetMapping("/order")
  public ResponseEntity<?> getOrderStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @GetMapping("/product")
  public ResponseEntity<?> getProductStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @GetMapping("/revenue")
  public ResponseEntity<?> getRevenueStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
