package com.example.mam.dto.order

import java.math.BigDecimal
import java.time.Instant

enum class OrderStatus{
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
data class OrderResponse(
    val id: Long,
    val shippingAddress: String,
    val totalPrice: BigDecimal,
    val note: String,
    val expectedDeliveryTime: Instant,
    val actualDeliveryTime: Instant,
    val orderStatus: OrderStatus,
    val userId: Long,
    val orderDetails: List<OrderDetailResponse>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val createdBy: Long,
    val updatedBy: Long
)
