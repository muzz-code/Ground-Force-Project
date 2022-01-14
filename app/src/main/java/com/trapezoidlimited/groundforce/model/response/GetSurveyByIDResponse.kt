package com.trapezoidlimited.groundforce.model.response

data class GetSurveyByIDResponse (
    val surveyId: String,
    val topic: String,
    val questions : List<String>
)
