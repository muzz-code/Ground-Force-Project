package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.model.request.SubmitSurveyRequest
import com.trapezoidlimited.groundforce.model.request.UpdateSurveyStatusRequest
import com.trapezoidlimited.groundforce.model.response.GenericResponseClass
import com.trapezoidlimited.groundforce.model.response.UpdateMissionStatusResponse
import retrofit2.http.*

interface SurveyApi {

    @PATCH("Survey/acceptance-status")
    suspend fun updateSurveyStatus(
        @Header("Authorization") token: String,
        @Body updateSurveyStatusRequest: UpdateSurveyStatusRequest
    ): GenericResponseClass<UpdateMissionStatusResponse>

    @POST("Survey/submit-survey")
    suspend fun submitSurvey(
        @Header("Authorization") token: String,
        @Body submitSurveyRequest: SubmitSurveyRequest
    ): GenericResponseClass<UpdateMissionStatusResponse>

    @GET("Survey/{agentId}/{status}/{page}")
    suspend fun getSurvey(
        @Header("Authorization") token: String,
        @Path("agentId") agentId: String,
        @Path("status") status: String,
        @Path("page") page: String
    ): GenericResponseClass<UpdateMissionStatusResponse>

}