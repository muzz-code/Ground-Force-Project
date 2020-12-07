package com.trapezoidlimited.groundforce.model.request

data class SubmitMissionRequest(
    val missionId: String,
    val buildingTypeId: String,
    val landmark: String,
    val busStop: String,
    val buildingColor: String,
    val addressExists: Boolean,
    val typeOfStructure: String,
    val longitude: String,
    val latitude: String,
    val remarks: String

)