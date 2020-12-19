package com.trapezoidlimited.groundforce.model.request

class ConfirmEmailAddressRequest(
    val emailAddress: String,
    val verificationCode: String
)
