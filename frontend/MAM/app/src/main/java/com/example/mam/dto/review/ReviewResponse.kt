package com.example.mam.dto.review

import java.time.Instant

data class ReviewResponse(
    val id: Long,
    val rate: Int,
    val content: String,
)
