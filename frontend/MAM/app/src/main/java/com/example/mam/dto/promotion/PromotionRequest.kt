package com.example.mam.dto.promotion

import java.math.BigDecimal
import java.time.Instant

data class PromotionRequest(
    val description: String,
    val discountValue: BigDecimal = BigDecimal.ZERO,
    val minValue: BigDecimal = BigDecimal.ZERO,
    val startDate: Instant,
    val endDate: Instant,
    val code: String
)
