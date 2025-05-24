package com.example.mam.services.dto.authorization.request

data class ForgetPasswordRequest (
    val username: String = "",
    val newPassword: String = "",
)