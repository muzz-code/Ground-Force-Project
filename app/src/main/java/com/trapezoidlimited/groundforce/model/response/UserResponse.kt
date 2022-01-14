package com.trapezoidlimited.groundforce.model.response

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val gender: String,
    val religion: String?,
    val email: String,
    val isVerified: Boolean,
    val additionalPhoneNumber: String?,
    val residentialAddress: String,
    val bankName: String?,
    val accountName: String?,
    val accountNumber: String?,
    val avatarUrl: String?,
    val publicId: String?,
    val isLocationVerified: Boolean
): ParentResponse