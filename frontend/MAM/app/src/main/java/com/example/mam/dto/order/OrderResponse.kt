package com.example.mam.dto.order

import com.example.mam.dto.BaseResponse
import java.math.BigDecimal
import java.time.Instant

data class OrderResponse(
    val shippingAddress: String,
    val totalPrice: BigDecimal,
    val note: String,
    val expectedDeliveryTime: String,
    val actualDeliveryTime: String,
    val orderStatus: String,
    val userId: Long,
    val orderDetails: List<OrderDetailResponse>,
): BaseResponse()
