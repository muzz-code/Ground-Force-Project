package com.trapezoidlimited.groundforce.model.request

data class UpdateSurveyStatusRequest(
    val agentId: String,
    val surveyId: String,
    val status: String
)