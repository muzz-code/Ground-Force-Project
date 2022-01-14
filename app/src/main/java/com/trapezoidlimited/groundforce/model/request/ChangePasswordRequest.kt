package com.trapezoidlimited.groundforce.model.request

data class ChangePasswordRequest(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
)