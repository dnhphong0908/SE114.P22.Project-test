package com.example.mam.dto.authorization.forgetpassword

data class ForgetPasswordRequest (
    val username: String = "",
    val newPassword: String = "",
)