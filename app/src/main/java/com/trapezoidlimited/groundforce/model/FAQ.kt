package com.trapezoidlimited.groundforce.model

data class FAQ(
    val question: String,
    val explanation: String,
    var isExpanded: Boolean = false
)