package com.se114p12.backend.controllers.stats;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.stats.RevenueStatsResponseDTO;
import com.se114p12.backend.services.stats.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "Statistics Module", description = "Endpoints for statistical data (user, order, product, revenue)")
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.API_BASE_PATH + "/stats")
public class StatsController {

  private final StatsService statsService;

  @Operation(summary = "Get user statistics", description = "Returns summary data related to users.")
  @ApiResponse(responseCode = "501", description = "Not implemented yet")
  @ErrorResponse
  @GetMapping("/user")
  public ResponseEntity<?> getUserStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Operation(summary = "Get order statistics", description = "Returns summary data related to orders.")
  @ApiResponse(responseCode = "501", description = "Not implemented yet")
  @ErrorResponse
  @GetMapping("/order")
  public ResponseEntity<?> getOrderStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Operation(summary = "Get product statistics", description = "Returns summary data related to products.")
  @ApiResponse(responseCode = "501", description = "Not implemented yet")
  @ErrorResponse
  @GetMapping("/product")
  public ResponseEntity<?> getProductStats() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Operation(
          summary = "Get revenue statistics",
          description = "Returns revenue statistics grouped by day, week, or month within a specific time range."
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successful retrieval of revenue statistics",
                  content = @Content(array = @ArraySchema(schema = @Schema(implementation = RevenueStatsResponseDTO.class))))
  })
  @ErrorResponse
  @GetMapping("/revenue")
  public ResponseEntity<List<RevenueStatsResponseDTO>> getRevenueStats(
          @Parameter(description = "Start date in ISO-8601 format (e.g., 2024-01-01T00:00:00Z)", required = false)
          @RequestParam(value = "startDate", required = false) Instant startDate,

          @Parameter(description = "End date in ISO-8601 format (e.g., 2024-01-31T23:59:59Z)", required = false)
          @RequestParam(value = "endDate", required = false) Instant endDate,

          @Parameter(description = "Group by 'day', 'week', or 'month'", example = "month", required = false)
          @RequestParam(value = "groupBy", required = false) String groupBy
  ) {
    return ResponseEntity.ok(statsService.getRevenueStats(startDate, endDate, groupBy));
  }
}