package com.example.mam.dto.user

import java.io.File

data class UserRequest(
    val fullName: String = "",
    val userName: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: File? = null,
    val roleId: Long = 0L,
)
