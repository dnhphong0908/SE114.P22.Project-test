package com.example.mam.dto.notification

import com.example.mam.dto.BaseResponse
import com.example.mam.entity.Notification
import java.time.Instant

data class NotificationResponse(
    val type: Notification,
    val title: String,
    val message: String,
    val status: Int,
    val userId: Long
): BaseResponse()
