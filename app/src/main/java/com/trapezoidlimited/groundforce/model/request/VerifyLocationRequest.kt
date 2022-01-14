package com.trapezoidlimited.groundforce.model.request

data class VerifyLocationRequest(
    val address: String,
    val state: String,
    val lga: String,
    val zipCode: String,
    val longitude: String,
    val latitude: String
)

