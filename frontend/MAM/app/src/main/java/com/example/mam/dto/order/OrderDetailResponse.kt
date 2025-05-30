package com.example.mam.dto.order

import java.math.BigDecimal

data class OrderDetailResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val productImage: String,
    val variationInfo: String,
    val quantity: Long,
    val price: BigDecimal,
    val totalPrice: BigDecimal,
    val orderId: Long
)
