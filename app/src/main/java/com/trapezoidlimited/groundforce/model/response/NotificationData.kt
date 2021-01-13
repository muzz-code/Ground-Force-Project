package com.trapezoidlimited.groundforce.model.response

data class NotificationData(
    val id: String,
    val notifications: String,
    val type: String,
    val addedBy: String,
    val updateBy: String,
    val date: String
)