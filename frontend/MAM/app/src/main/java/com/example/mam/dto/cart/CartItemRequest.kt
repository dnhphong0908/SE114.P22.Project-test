package com.example.mam.dto.cart

import java.math.BigDecimal

data class CartItemRequest(
    val cartId: Long = 0L,
    val productId: Long = 0L,
    val quantity: Long = 0L,
    val price: BigDecimal = BigDecimal.ZERO,
    val variationOptionIds: Set<Long> = emptySet()

)
