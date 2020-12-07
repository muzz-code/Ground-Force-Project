package com.trapezoidlimited.groundforce.model.response

data class GetMissionResponse(
    val pageMetaData: PageMetaData,
    val data: List<Mission>
): ParentResponse