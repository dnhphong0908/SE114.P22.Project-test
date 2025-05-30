package com.example.mam.dto.cart

import com.example.mam.dto.BaseResponse
import java.math.BigDecimal

data class CartItemResponse(
    val cartId: Long = 0L,
    val productId: Long = 0L,
    val quantity: Long = 0L,
    val price: BigDecimal = BigDecimal.ZERO,
    val variationOptionIds: Set<Long> = emptySet()
): BaseResponse()
