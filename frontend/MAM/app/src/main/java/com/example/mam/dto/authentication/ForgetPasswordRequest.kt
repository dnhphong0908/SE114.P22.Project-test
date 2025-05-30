package com.example.mam.dto.authentication

data class ForgetPasswordRequest (
    val username: String = "",
    val newPassword: String = "",
)