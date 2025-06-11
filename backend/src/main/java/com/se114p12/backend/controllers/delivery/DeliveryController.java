package com.se114p12.backend.controllers.delivery;

import com.se114p12.backend.dtos.delivery.DeliveryRequestDTO;
import com.se114p12.backend.dtos.delivery.DeliveryResponseDTO;
import com.se114p12.backend.services.delivery.MapService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    private final MapService mapboxService;

    @PostMapping("/estimate-time")
    public ResponseEntity<DeliveryResponseDTO> estimateTime(@RequestBody DeliveryRequestDTO request) {
        DeliveryResponseDTO result = mapboxService.calculateExpectedDeliveryTime(
                request.getOriginLat(),
                request.getOriginLng(),
                request.getDestinationLat(),
                request.getDestinationLng()
        );
        return ResponseEntity.ok(result);
    }
}
