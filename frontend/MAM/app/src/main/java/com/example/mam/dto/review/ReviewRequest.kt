package com.example.mam.dto.review
data class ReviewRequest(
    val userId: Long,
    val productId: Long,
    val rate: Int,
    val content: String,
    val reply: String
)
