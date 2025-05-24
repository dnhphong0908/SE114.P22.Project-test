package com.example.mam.services

import com.example.mam.entity.Notification
import com.example.mam.entity.Order
import com.example.mam.entity.User
import com.example.mam.dto.signin.SignInRequest
import com.example.mam.dto.signin.SignInResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface Authpublicservice {
    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): SignInResponse
    @POST("auth/register")
    suspend fun signUp(@Body request: SignInRequest)
    
}