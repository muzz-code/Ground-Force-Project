package com.trapezoidlimited.groundforce.model.request

import com.trapezoidlimited.groundforce.model.response.ParentResponse

data class VerifyPhoneRequest(
    val phoneNumber: String?
)