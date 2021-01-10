package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RoomRepository {
    suspend fun addAgent(agent: RoomAgent)
    suspend fun addAdditionalDetail(additionalDetail: RoomAdditionalDetail)
    suspend fun updateAgent(agent: RoomAgent)
    suspend fun updateAdditionalDetail(additionalDetail: RoomAdditionalDetail)
    suspend fun addMission(mission: RoomMission)
    suspend fun addHistoryMission(mission: RoomHistoryMission)
    fun readAllHistoryMissions(): LiveData<List<RoomHistoryMission>>
    suspend fun deleteAllHistoryMissions()
    suspend fun deleteByHistoryMissionId(historyMissionId: String)

    suspend fun addHistorySurvey(survey: RoomHistorySurvey)
    fun readAllHistorySurveys(): LiveData<List<RoomHistorySurvey>>
    suspend fun deleteAllHistorySurveys()
    suspend fun deleteByHistorySurveyId(historySurveyId: String)

    suspend fun addNotification(notification: RoomNotification)
    fun readAllNotifications(): LiveData<List<RoomNotification>>
    suspend fun deleteAllNotifications()
    suspend fun deleteByNotificationId(notificationId: String)

    fun readAgent(): LiveData<List<RoomAgent>>
    fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>>
    fun readAllMissions(): LiveData<List<RoomMission>>
    fun readAllOngoingMissions(): LiveData<List<RoomOngoingMission>>
    suspend fun deleteAllMissions()
    suspend fun deleteByMissionId(missionId: String)
    suspend fun addOngoingMission(ongoingMission: RoomOngoingMission)
    suspend fun deleteAllOngoingMissions()
    suspend fun deleteByOngoingMissionId(missionId: String)
    suspend fun deleteAllAgentDetails()
    suspend fun readAgentA(): List<RoomAgent>

    suspend fun addSurvey(survey: RoomSurvey)

    fun readAllSurveys(): LiveData<List<RoomSurvey>>

    suspend fun deleteAllSurveys()

    suspend fun deleteBySurveyId(surveyId: String)
}