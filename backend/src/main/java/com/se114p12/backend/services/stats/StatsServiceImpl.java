package com.se114p12.backend.services.stats;

import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.repositories.order.OrderRepository;
import java.time.Instant;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

  private final OrderRepository orderRepository;

  public List<RevenueStatsResponseDTO> getRevenueStats(Instant start, Instant end, String groupBy) {

    List<Order> orders = orderRepository.findByCreatedAtOptional(start, end);

    Map<String, List<Order>> groupedOrders = groupOrders(orders, groupBy);

    List<RevenueStatsResponseDTO> result = new ArrayList<>();

    for (Map.Entry<String, List<Order>> entry : groupedOrders.entrySet()) {
      String key = entry.getKey();
      List<Order> orderList = entry.getValue();
      RevenueStatsResponseDTO stats = new RevenueStatsResponseDTO();
      stats.setPeriod(key);
      stats.setTotalRevenue(
          orderList.stream().mapToDouble(order -> order.getTotalPrice().doubleValue()).sum());
      stats.setOrderCount(orderList.size());
      result.add(stats);
    }

    return result;
  }

  public Map<String, List<Order>> groupOrders(List<Order> orders, String groupBy) {
    Map<String, List<Order>> groupedOrders;

    switch (groupBy.toLowerCase()) {
      case "month":
        groupedOrders =
            orders.stream()
                .collect(
                    Collectors.groupingBy(
                        order -> YearMonth.from(order.getCreatedAt()).toString()));
        break;

      case "year":
        groupedOrders =
            orders.stream()
                .collect(
                    Collectors.groupingBy(order -> Year.from(order.getCreatedAt()).toString()));
        break;

      default:
        groupedOrders =
            orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().toString()));
    }

    return groupedOrders;
  }
}
