package com.trapezoidlimited.groundforce.model.request

data class VerifyAccountRequest(
    val bankCode: String,
    val accountNumber: String,
    val additionalPhoneNumber: String?,
    val gender: String
)