package com.trapezoidlimited.groundforce.utils

import com.trapezoidlimited.groundforce.data.SurveyData
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.mission.OngoingItem


object DummyData {

    fun missionLocationData(): MutableList<MissionItem> {
        return mutableListOf(
            MissionItem("Ikeja A GRA", "31, Opebi Street, Ikeja, Lagos "),
            MissionItem("Ikeja B GRA", "31, Opebi Street, Ikeja, Lagos "),
            MissionItem("Ikeja C GRA", "31, Opebi Street, Ikeja, Lagos "),
            MissionItem("Ikeja D GRA", "31, Opebi Street, Ikeja, Lagos "),
            MissionItem("Ikeja E GRA", "31, Opebi Street, Ikeja, Lagos "),
            MissionItem("Ikeja F GRA", "31, Opebi Street, Ikeja, Lagos ")
        )
    }

    fun ongoingLocationData(): MutableList<OngoingItem> {
        return mutableListOf(
            OngoingItem("Ikeja A GRA", "31, Opebi Street, Ikeja, Lagos "),
            OngoingItem("Ikeja B GRA", "31, Opebi Street, Ikeja, Lagos "),
            OngoingItem("Ikeja C GRA", "31, Opebi Street, Ikeja, Lagos "),
            OngoingItem("Ikeja D GRA", "31, Opebi Street, Ikeja, Lagos "),
            OngoingItem("Ikeja E GRA", "31, Opebi Street, Ikeja, Lagos ")
        )
    }


    val surveyList = mutableListOf(
        SurveyData(
            "Human Resource Survey 2014",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            40
        ),
        SurveyData(
            "Human Resource Survey 2015",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            50
        ),
        SurveyData(
            "Human Resource Survey 2016",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            60
        ),
        SurveyData(
            "Human Resource Survey 2017",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            70
        ),
        SurveyData(
            "Human Resource Survey 2018",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            80
        ),
        SurveyData(
            "Human Resource Survey 2019",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            90
        ),
        SurveyData(
            "Human Resource Survey 2020",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            100
        ),
        SurveyData(
            "Human Resource Survey 2021",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            10
        ),
        SurveyData(
            "Human Resource Survey 2022",
            "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...",
            20
        )
    )


}