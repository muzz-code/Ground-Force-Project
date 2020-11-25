package com.trapezoidlimited.groundforce.data

data class LoginSuccessResponse(
    var token: String?,
    var id: String?
) : ParentResponse