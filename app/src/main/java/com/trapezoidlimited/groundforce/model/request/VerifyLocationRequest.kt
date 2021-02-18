package com.trapezoidlimited.groundforce.model.request

data class VerifyLocationRequest(
    val address: String,
    val longitude: String,
    val latitude: String
)

