package com.example.mam.dto.authorization.signin

data class SignInResponse (
    val accessToken: String,
    val refreshToken: String
)