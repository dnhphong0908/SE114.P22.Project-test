package com.example.mam.dto.authentication

data class VerifyOTPRequest(
    val email: String,
    val otp: String,
    val action: OTPAction,
)
enum class OTPAction {
    VERIFY_PHONE,
    FORGOT_PASSWORD,
}
