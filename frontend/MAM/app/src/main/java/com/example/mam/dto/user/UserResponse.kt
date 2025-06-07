package com.example.mam.dto.user

import com.example.mam.dto.BaseResponse
import com.example.mam.dto.role.RoleResponse

enum class UserStatus {
    PENDING,
    ACTIVE,
    INACTIVE,
    DELETED
}
enum class LoginProvider {
    LOCAL,
    GOOGLE,
}
data class UserResponse(
    val fullname: String = "",
    val username: String = "",
    val email: String = "",
    val phone: String = "",
    val avatarUrl: String? = null,
    val status: UserStatus = UserStatus.PENDING,
    val loginProvider: LoginProvider = LoginProvider.LOCAL,
    val role: RoleResponse = RoleResponse()
): BaseResponse()
