package com.trapezoidlimited.groundforce.model.response

import com.trapezoidlimited.groundforce.model.request.Question

data class GetSurveyResponse(
    val question: List<Question>
): ParentResponse