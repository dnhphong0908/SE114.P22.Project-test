package com.example.mam.dto.authentication

data class SignUpRequest(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = ""
)
