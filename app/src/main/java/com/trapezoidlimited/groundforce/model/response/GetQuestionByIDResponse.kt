package com.trapezoidlimited.groundforce.model.response

data class GetQuestionByIDResponse(
    val surveyQuestionId: String,
    val surveyId: String,
    val question: String,
    val options: List<Option>
)


