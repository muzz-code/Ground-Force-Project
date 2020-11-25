package com.trapezoidlimited.groundforce.model.request

data class ConfirmPhoneRequest (
    val phoneNumber: String,
    val verifyCode: String
)