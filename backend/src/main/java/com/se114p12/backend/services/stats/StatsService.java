package com.se114p12.backend.services.stats;

import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import java.time.Instant;
import java.util.List;

public interface StatsService {

  List<RevenueStatsResponseDTO> getRevenueStats(Instant startDate, Instant endDate, String groupBy);
}
