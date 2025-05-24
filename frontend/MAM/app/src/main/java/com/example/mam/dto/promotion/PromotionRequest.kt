package com.example.mam.dto.promotion

import java.time.Instant

data class PromotionRequest(
    val description: String,
    val discountAmount: Float,
    val minValue: Double,
    val startDate: Instant,
    val endDate: Instant,
    val code: String
)
