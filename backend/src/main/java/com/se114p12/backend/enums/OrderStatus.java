package com.se114p12.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    PENDING(1),
    CONFIRMED(2),
    PROCESSING(3),
    SHIPPING(4),
    COMPLETED(5),
    CANCELED(6);

    private Integer code;
}
