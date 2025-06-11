package com.se114p12.backend.services.stats;

import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.repositories.order.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
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
      case "quarter":
        groupedOrders =
            orders.stream()
                .collect(
                    Collectors.groupingBy(
                        order -> {
                          LocalDate date = LocalDate.from(order.getCreatedAt());
                          int month = date.getMonthValue();
                          int quarter = (month - 1) / 3 + 1;
                          return "" + date.getYear() + "-Q" + quarter;
                        }));
        break;

      default:
        groupedOrders =
            orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().toString()));
    }

    return groupedOrders;
  }

  @Override
  public Map<Integer, BigDecimal> getRevenueStatsByYear(int year, String groupBy) {
    List<Order> orders = orderRepository.findByYear(year);

    switch (groupBy.toLowerCase()) {
      case "month":
        return orders.stream()
            .collect(
                Collectors.groupingBy(
                    order -> Month.from(order.getCreatedAt()).getValue(),
                    Collectors.reducing(BigDecimal.ZERO, Order::getTotalPrice, BigDecimal::add)));
      case "quarter":
        return orders.stream()
            .collect(
                Collectors.groupingBy(
                    order -> {
                      int month = Month.from(order.getCreatedAt()).getValue();
                      return (month - 1) / 3 + 1; // Calculate quarter
                    },
                    Collectors.reducing(BigDecimal.ZERO, Order::getTotalPrice, BigDecimal::add)));
      default:
        throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
    }
  }

  @Override
  public Map<String, BigDecimal> getSoldProductCountByCategory(int month, int year) {
    List<Order> orders = orderRepository.findByMonthAndYear(month, year);
    return orders.stream()
        .flatMap(order -> order.getOrderDetails().stream())
        .collect(
            Collectors.groupingBy(
                orderItem -> orderItem.getCategoryName(),
                Collectors.reducing(
                    BigDecimal.ZERO,
                    orderItem -> BigDecimal.valueOf(orderItem.getQuantity()),
                    BigDecimal::add)));
  }
}
