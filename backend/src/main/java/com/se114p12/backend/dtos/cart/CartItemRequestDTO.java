package com.se114p12.backend.dtos.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartItemRequestDTO {
    private Long cartId;
    private Long productId;
    private Long quantity;
    private BigDecimal price;
    private Set<Long> variationOptionIds;
}
