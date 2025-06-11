package com.se114p12.backend.controllers.delivery;

import com.se114p12.backend.configs.ShopLocationConfig;
import com.se114p12.backend.dtos.delivery.DeliveryRequestDTO;
import com.se114p12.backend.dtos.delivery.DeliveryResponseDTO;
import com.se114p12.backend.services.delivery.MapService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    private final MapService mapboxService;
    private final ShopLocationConfig shopLocationConfig;

    @PostMapping("/estimate-time")
    public ResponseEntity<DeliveryResponseDTO> estimateTime(@RequestBody DeliveryRequestDTO request) {
        double originLat = shopLocationConfig.getLat();
        double originLng = shopLocationConfig.getLng();

        DeliveryResponseDTO result = mapboxService.calculateExpectedDeliveryTime(
                originLat,
                originLng,
                request.getDestinationLat(),
                request.getDestinationLng()
        );

        return ResponseEntity.ok(result);
    }
}