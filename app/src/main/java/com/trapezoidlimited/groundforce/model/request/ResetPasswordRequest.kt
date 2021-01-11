package com.trapezoidlimited.groundforce.model.request

data class ResetPasswordRequest(
    val email: String,
    val token: String,
    val newPassword: String
)