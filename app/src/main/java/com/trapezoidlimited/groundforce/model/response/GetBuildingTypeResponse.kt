package com.trapezoidlimited.groundforce.model.response

import com.trapezoidlimited.groundforce.room.RoomBuildingType

data class GetBuildingTypeResponse(
    val pageMetaData: PageMetaData,
    val data: List<BuildingType>
)