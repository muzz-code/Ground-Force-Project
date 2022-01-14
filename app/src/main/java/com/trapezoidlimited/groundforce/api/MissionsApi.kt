package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.model.request.SubmitMissionRequest
import com.trapezoidlimited.groundforce.model.response.*
import retrofit2.http.*

interface MissionsApi {

    @GET("Mission/{agentId}/{status}/{page}")
    suspend fun getMissions(
        @Header("Authorization") token: String,
        @Path("agentId") agentId: String,
        @Path("status") status: String,
        @Path("page") page: String
    ): GenericResponseClass<GetMissionResponse>

    @PATCH("Mission/{missionId}/acceptance-status/{status}")
    suspend fun updateMissionStatus(
        @Header("Authorization") token: String,
        @Path("missionId") missionId: String,
        @Path("status") status: String
    ): GenericResponseClass<UpdateMissionStatusResponse>

    @POST("Mission/submit")
    suspend fun submitMission(
        @Header("Authorization") token: String,
        @Body submitMissionRequest: SubmitMissionRequest
    ): GenericResponseClass<SubmitMissionResponse>

    @GET("Mission/building-types/{page}")
    suspend fun getBuildingType(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): GenericResponseClass<GetBuildingTypeResponse>
}