package com.se114p12.backend.dtos.stats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatsResponseDTO {
  private Long totalOrders;
  private Long completedOrders;
  private Long pendingOrders;
  private Long cancelledOrders;
}
