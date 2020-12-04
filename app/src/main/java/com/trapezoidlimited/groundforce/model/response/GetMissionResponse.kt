package com.trapezoidlimited.groundforce.model.response

data class GetMissionResponse(
    val message: List<Mission>
): ParentResponse