package com.example.mam.services

import com.example.mam.dto.authentication.SignInRequest
import com.example.mam.dto.authentication.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthPublicService {
    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): SignInResponse
    @POST("auth/register")
    suspend fun signUp(@Body request: SignInRequest)
    
}