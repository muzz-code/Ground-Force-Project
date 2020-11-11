package com.trapezoidlimited.groundforce.model

data class ForgotPasswordResponse(
    val message : String = ""
)


data class ResponseClass<T>(
    val status:Long,
    val message : String?,
    val payload:T?
)
