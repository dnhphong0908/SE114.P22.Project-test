package com.example.mam.dto.vo

enum class ErrorType(var message: String) {
    VALIDATION_ERROR("Validation error occurred"),
    MISSING_FIELD_ERROR("Required field is missing"),
    INVALID_INPUT_ERROR("Input provided is invalid"),
    AUTHENTICATION_ERROR("Authentication failed"),
    AUTHORIZATION_ERROR("Authorization failed"),
    RESOURCE_NOT_FOUND_ERROR("Resource not found"),
    RESOURCE_CONFLICT_ERROR("Resource conflict occurred"),
    RESOURCE_GONE_ERROR("Resource is no longer available"),
    SERVER_ERROR("Internal server error"),
    BAD_REQUEST_ERROR("Bad request");

    fun ErrorType(message: String) {
        this.message = message
    }
}
data class ErrorVO(
    val type: ErrorType,
    val details: Object
)
