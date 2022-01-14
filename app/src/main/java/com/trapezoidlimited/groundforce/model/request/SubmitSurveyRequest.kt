package com.trapezoidlimited.groundforce.model.request

class SubmitSurveyRequest(
    val agentId: String,
    val surveyId: String,
    val questions: List<Question>
)
