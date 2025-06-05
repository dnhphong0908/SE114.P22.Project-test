package com.example.mam.dto.authentication

data class AuthResponse (
    val accessToken: String,
    val refreshToken: String
)