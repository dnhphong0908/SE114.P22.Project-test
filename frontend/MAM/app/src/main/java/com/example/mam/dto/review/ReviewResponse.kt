package com.example.mam.dto.review

import java.time.Instant

data class ReviewResponse(
    val id: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
    val user: Long,
    val productId: Long,
    val rate: Int,
    val content: String,
)
