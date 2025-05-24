package com.example.mam.services.dto.authorization.signin

data class SignInResponse (
    val accessToken: String,
    val refreshToken: String
)