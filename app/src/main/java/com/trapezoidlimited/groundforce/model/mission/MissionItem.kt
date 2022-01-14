package com.trapezoidlimited.groundforce.model.mission

import com.trapezoidlimited.groundforce.model.response.ParentMission

data class MissionItem(
    val locationTitle: String,
    val locationSubTitle: String,
    val id: String? = null,
    val date: String? = null,
): ParentMission