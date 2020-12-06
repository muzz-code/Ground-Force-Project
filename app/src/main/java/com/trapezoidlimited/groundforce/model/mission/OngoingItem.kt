package com.trapezoidlimited.groundforce.model.mission

data class OngoingItem(
    val locationTitle: String,
    val locationSubTitle: String,
    val id: String? = null,
    val date: String? = null,
)