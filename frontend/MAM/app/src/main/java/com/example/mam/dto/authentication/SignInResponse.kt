package com.example.mam.dto.authentication

data class SignInResponse (
    val accessToken: String,
    val refreshToken: String
)