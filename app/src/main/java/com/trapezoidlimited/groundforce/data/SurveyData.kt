package com.trapezoidlimited.groundforce.data

data class SurveyData(
    var title: String,
    var body: String,
    var progress: Int,
    val id: String? = null
)