package com.example.mam.dto.notification

import androidx.annotation.Nullable
import com.example.mam.entity.Notification

data class NotificationRequest(
    val usersIds: List<Long>,
    val type: Notification,
    val title: String,
    val message: String,
    val status: Int?
)
