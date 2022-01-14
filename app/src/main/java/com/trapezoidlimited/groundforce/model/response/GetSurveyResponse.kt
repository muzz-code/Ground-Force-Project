package com.trapezoidlimited.groundforce.model.response

import com.trapezoidlimited.groundforce.model.request.Question

data class GetSurveyResponse(
    val pageMetaData: PageMetaData,
    val userSurveyToReturn: List<UserSurveyToReturn>
): ParentResponse