package com.example.mam.services

import com.example.mam.dto.notification.NotificationRequest
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationService {
    @POST("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: Long): Response<Void>
    @POST("notifications/send")
    suspend fun sendNotification(
        @Body notificationRequest: NotificationRequest
    ): Response<NotificationResponse>
    @POST("notifications/me/read-all")
    suspend fun markAllMyNotificationsAsRead(): Response<Void>
    @GET("notifications")
    suspend fun getAllNotifications(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String? = null
    ): Response<PageVO<NotificationResponse>>
    @GET("notifications/me")
    suspend fun getMyNotifications(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String ?= null
    ):Response<PageVO<NotificationResponse>>
    @GET("notifications/me/unread-count")
    suspend fun countMyUnreadNotifications():Response<Long>
    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Long): Response<Void>
}