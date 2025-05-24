package com.example.mam.services.dto.authorization.forgetpassword

data class ForgetPasswordRequest (
    val username: String = "",
    val newPassword: String = "",
)