package com.trapezoidlimited.groundforce.model.response

data class LoginResponse(
    val loginToken: LoginToken
) : ParentResponse
