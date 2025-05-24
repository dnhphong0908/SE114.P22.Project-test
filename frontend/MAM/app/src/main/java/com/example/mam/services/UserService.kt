package com.example.mam.services

import com.example.mam.dto.user.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users/{userId}")
    suspend fun getUserById(@Path("id") userId: String): Response<UserResponse>
}