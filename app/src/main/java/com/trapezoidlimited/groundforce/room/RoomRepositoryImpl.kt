package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RoomRepositoryImpl(private val dao: RoomDao) : RoomRepository {
    override suspend fun addAgent(agent: RoomAgent) {
        dao.addAgent(agent)
    }

    override suspend fun addAdditionalDetail(additionalDetail: RoomAdditionalDetail) {
        dao.addAdditionalDetail(additionalDetail)
    }

    override suspend fun updateAgent(agent: RoomAgent) {
        dao.updateAgent(agent)
    }

    override suspend fun updateAdditionalDetail(additionalDetail: RoomAdditionalDetail) {
        dao.updateAdditionalDetail(additionalDetail)
    }

    override suspend fun addMission(mission: RoomMission) {
        dao.addMission(mission)
    }

    override suspend fun addHistoryMission(mission: RoomHistoryMission) {
        dao.addHistoryMission(mission)
    }

    override fun readAllHistoryMissions(): LiveData<List<RoomHistoryMission>> {
       return dao.readAllHistoryMissions()
    }

    override suspend fun deleteAllHistoryMissions() {
        dao.deleteAllHistoryMissions()
    }

    override suspend fun deleteByHistoryMissionId(historyMissionId: String) {
        dao.deleteByHistoryMissionId(historyMissionId)
    }

    override suspend fun addHistorySurvey(survey: RoomHistorySurvey) {
        dao.addHistorySurvey(survey)
    }

    override fun readAllHistorySurveys(): LiveData<List<RoomHistorySurvey>> {
        return dao.readAllHistorySurveys()
    }

    override suspend fun deleteAllHistorySurveys() {
        dao.deleteAllHistorySurveys()
    }

    override suspend fun deleteByHistorySurveyId(historySurveyId: String) {
        dao.deleteByHistorySurveyId(historySurveyId)
    }

    override suspend fun addNotification(notification: RoomNotification) {
        dao.addNotification(notification)
    }

    override fun readAllNotifications(): LiveData<List<RoomNotification>> {
        return dao.readAllAddNotifications()
    }

    override suspend fun deleteAllNotifications() {
        dao.deleteAllAddNotifications()
    }

    override suspend fun deleteByNotificationId(notificationId: String) {
        dao.deleteByNotificationId(notificationId)
    }

    override fun readAgent(): LiveData<List<RoomAgent>> {
        return dao.readAgent()
    }

    override fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>> {
        return dao.readAdditionalDetail()
    }

    override fun readAllMissions(): LiveData<List<RoomMission>> {
       return dao.readAllMissions()
    }

    override fun readAllOngoingMissions(): LiveData<List<RoomOngoingMission>> {
        return dao.readAllOngoingMissions()
    }


    override suspend fun deleteAllMissions(){
        dao.deleteAllMissions()
    }

    override suspend fun deleteByMissionId(missionId: String) {
        dao.deleteByMissionId(missionId)
    }

    override suspend fun addOngoingMission(ongoingMission: RoomOngoingMission) {
        dao.addOngoingMission(ongoingMission)
    }

    override suspend fun deleteAllOngoingMissions() {
        dao.deleteAllOngoingMissions()
    }

    override suspend fun deleteByOngoingMissionId(missionId: String) {
        dao.deleteByOngoingMissionId(missionId)
    }

    override suspend fun deleteAllAgentDetails() {
        dao.deleteAllAgentDetails()
    }

    override suspend fun readAgentA(): List<RoomAgent> {
       return dao.readAgentA()
    }

    override suspend fun addSurvey(survey: RoomSurvey) {
        return dao.addSurvey(survey)
    }

    override fun readAllSurveys(): LiveData<List<RoomSurvey>> {
        return dao.readAllSurveys()
    }

    override suspend fun deleteAllSurveys() {
        return dao.deleteAllSurveys()
    }

    override suspend fun deleteBySurveyId(surveyId: String) {
        return dao.deleteBySurveyId(surveyId)
    }


}