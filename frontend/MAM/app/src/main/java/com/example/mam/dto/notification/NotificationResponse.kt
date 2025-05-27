package com.example.mam.dto.notification

import com.example.mam.entity.Notification
import java.time.Instant

data class NotificationResponse(
    val id: Long,
    val type: Notification,
    val title: String,
    val message: String,
    val status: Int,
    val createdAt: Instant,
    val userId: Long
)
