package com.trapezoidlimited.groundforce.model

import com.trapezoidlimited.groundforce.data.LoginSuccessResponse

data class GenericResponseClass(
    val title: String?,
    val errors: String?,
    val data: LoginSuccessResponse?
)



