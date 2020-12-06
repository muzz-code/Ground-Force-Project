package com.trapezoidlimited.groundforce.model.response

data class Mission(
    val missionId: String,
    val itemId: String,
    val title: String,
    val description: String,
    val addedBy: String?,
    val createdAt: String
)
