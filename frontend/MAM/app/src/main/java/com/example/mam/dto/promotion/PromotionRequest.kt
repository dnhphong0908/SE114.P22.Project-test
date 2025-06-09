package com.example.mam.dto.promotion

import java.math.BigDecimal
import java.time.Instant

data class PromotionRequest(
    val description: String,
    val discountValue: BigDecimal,
    val minValue: BigDecimal,
    val startDate: Instant,
    val endDate: Instant,
    val code: String
)
