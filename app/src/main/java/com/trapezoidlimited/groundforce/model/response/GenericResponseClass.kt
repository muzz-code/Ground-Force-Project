package com.trapezoidlimited.groundforce.model.response

data class GenericResponseClass<T>(
    val title: String?,
    val errors: ErrorResponse?,
    val data: T?
)



