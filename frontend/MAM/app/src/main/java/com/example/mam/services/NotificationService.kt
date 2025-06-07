package com.example.mam.services

import com.example.mam.dto.notification.NotificationRequest
import com.example.mam.dto.notification.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {
    @POST("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: Long): Response<Unit>
    @POST("notifications/send")
    suspend fun sendNotification(
        @Body notificationRequest: NotificationRequest
    ): Response<NotificationResponse>
    @POST("notifications/me/read-all")
    suspend fun markAllMyNotificationsAsRead(): Response<Unit>
    @GET("notifications/me")
    suspend fun getMyNotifications():Response<List<NotificationResponse>>
    @GET("notifications/me/unread-count")
    suspend fun countMyUnreadNotifications():Response<Int>
    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Long): Response<Unit>
}