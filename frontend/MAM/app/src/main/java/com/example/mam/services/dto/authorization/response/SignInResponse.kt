package com.example.mam.services.dto.authorization.response

data class SignInResponse (
    val accessToken: String,
    val refreshToken: String
)