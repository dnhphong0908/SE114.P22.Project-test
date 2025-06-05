package com.example.mam.services

import com.example.mam.dto.authentication.SendOTPRequest
import com.example.mam.dto.authentication.SignInRequest
import com.example.mam.dto.authentication.VerifyOTPRequest
import com.example.mam.dto.authentication.AuthResponse
import com.example.mam.dto.authentication.RefreshTokenRequest
import com.example.mam.dto.authentication.SignUpRequest
import com.example.mam.dto.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthPublicService {
    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): Response<AuthResponse>
    @POST("auth/register")
    suspend fun signUp(@Body request: SignUpRequest): Response<UserResponse>
    @POST("auth/send-otp")
    suspend fun sendOtp(@Body request: SendOTPRequest): Response<String>
    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOTPRequest): Response<Void>
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>
}