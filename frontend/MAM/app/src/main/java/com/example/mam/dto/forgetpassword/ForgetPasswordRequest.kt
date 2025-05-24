package com.example.mam.dto.forgetpassword

data class ForgetPasswordRequest (
    val username: String = "",
    val newPassword: String = "",
)