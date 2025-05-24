package com.example.mam.dto.user

data class UserRequest(
    val fullName: String,
    val userName: String,
    val email: String,
    val phone: String,
    val avatarUrl: String,
    val roleId: Long
)
