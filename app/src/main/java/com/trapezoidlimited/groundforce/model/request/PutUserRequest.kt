package com.trapezoidlimited.groundforce.model.request

data class PutUserRequest(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val gender: String,
    val additionalPhoneNumber: String,
    val religion: String
)