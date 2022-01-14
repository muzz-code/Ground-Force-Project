package com.trapezoidlimited.groundforce.utils

import com.trapezoidlimited.groundforce.data.PaymentData
import com.trapezoidlimited.groundforce.data.SurveyData
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.mission.OngoingItem
import com.trapezoidlimited.groundforce.model.mission.SurveyItem


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

    fun historyMissionData(): MutableList<MissionItem> {
        return mutableListOf(
            MissionItem("Makoro, Eti-Osa", "10 Aremo Adesegun Oniru Crescent", "6 Sept, 2020"),
            MissionItem("Allen, Ikeja", "7 Gbemisola St, Allen 100281", "6 Sept, 2020")
        )
    }

    fun historySurveyData(): MutableList<SurveyItem> {
        return mutableListOf(
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos..."),
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos..."),
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos..."),
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos..."),
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos..."),
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos..."),
            SurveyItem("Human Resource Survey 2020", "This survey is to capture the opinions of Nigerian low level employees in the lorem ipsum dolor sit abet duos...")
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


    var paymentList = mutableListOf(
        PaymentData(100, "You received NGN 100 ", "13 Sept, 2020"),
        PaymentData(200, "You received NGN 200 ", "14 Sept, 2020"),
        PaymentData(300, "You received NGN 300 ", "15 Sept, 2020"),
        PaymentData(400, "You received NGN 400 ", "16 Sept, 2020")
    )

}