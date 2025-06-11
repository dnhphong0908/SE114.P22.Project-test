package com.se114p12.backend.dtos.order;

import com.se114p12.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {
    @NotNull
    private Double destinationLatitude;

    @NotNull
    private Double destinationLongitude;

    private String note;

    @NotNull
    private PaymentMethod paymentMethod;

    private Long promotionId;
}
