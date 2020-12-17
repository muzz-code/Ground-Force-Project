package com.trapezoidlimited.groundforce.model.response

data class GenericErrorClass(
    val errors: Errors,
    val type: String,
    val title: String,
    val status: Long,
    val traceID: String
)

data class Errors(
    val id: List<String>
)
