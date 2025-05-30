package com.example.mam.dto.promotion

import com.example.mam.dto.BaseResponse
import java.time.Instant

data class PromotionResponse(
    val description: String,
    val discountAmount: Float,
    val minValue: Double,
    val startDate: Instant,
    val endDate: Instant,
    val code: String
): BaseResponse()
