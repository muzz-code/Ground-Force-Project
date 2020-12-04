package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.model.request.SubmitMissionRequest
import com.trapezoidlimited.groundforce.model.response.GenericResponseClass
import com.trapezoidlimited.groundforce.model.response.GetMissionResponse
import com.trapezoidlimited.groundforce.model.response.SubmitMissionResponse
import com.trapezoidlimited.groundforce.model.response.UpdateMissionStatusResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MissionsApi {

    @GET("Mission/{agentId}/{status}/{page}")
    suspend fun getMissions(
        @Path("agentId") agentId: String,
        @Path("status") status: String,
        @Path("page") page: String
    ): GenericResponseClass<GetMissionResponse>

    @PATCH("Mission/{missionId}/acceptance-status/{status}")
    suspend fun updateMissionStatus(
        @Path("missionId") missionId: String,
        @Path("status") status: String
    ): GenericResponseClass<UpdateMissionStatusResponse>

    @GET("Mission/submit")
    suspend fun submitMission(
        @Body submitMissionRequest: SubmitMissionRequest
    ): GenericResponseClass<SubmitMissionResponse>
}