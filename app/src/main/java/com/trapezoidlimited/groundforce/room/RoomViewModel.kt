package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trapezoidlimited.groundforce.data.AgentObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository) : ViewModel() {


    var agentObject: LiveData<List<RoomAgent>> =  repository.readAgent()

    val additionalDetail: LiveData<List<RoomAdditionalDetail>> = repository.readAdditionalDetail()

    var mission: LiveData<List<RoomMission>> = repository.readAllMissions()

    var historyMission: LiveData<List<RoomHistoryMission>> = repository.readAllHistoryMissions()

    var historySurvey: LiveData<List<RoomHistorySurvey>> = repository.readAllHistorySurveys()

    var notifications: LiveData<List<RoomNotification>> = repository.readAllNotifications()

    var survey: LiveData<List<RoomSurvey>> = repository.readAllSurveys()

    var ongoingMission: LiveData<List<RoomOngoingMission>> = repository.readAllOngoingMissions()

    private var _agentObjectA: MutableLiveData<List<RoomAgent>> = MutableLiveData()
    val agentObjectA: LiveData<List<RoomAgent>>
        get() = _agentObjectA


    fun addAgent(agent: RoomAgent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAgent(agent)
        }
    }

    fun addAdditionalDetail(additionalDetail: RoomAdditionalDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAdditionalDetail(additionalDetail)
        }
    }

    fun updateAgent(agent: RoomAgent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAgent(agent)
        }
    }

    fun updateAdditionalDetail(additionalDetail: RoomAdditionalDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAdditionalDetail(additionalDetail)
        }
    }

    fun addMission(mission: RoomMission) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMission(mission)
        }
    }

    fun addHistoryMission(historyMission: RoomHistoryMission) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHistoryMission(historyMission)
        }
    }

    fun deleteAllHistoryMission() {
        viewModelScope.launch {
            repository.deleteAllHistoryMissions()
        }
    }


    fun addNotification(notification: RoomNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNotification(notification)
        }
    }


    fun deleteAllNotifications() {
        viewModelScope.launch {
            repository.deleteAllNotifications()
        }
    }

    fun deleteByNotificationId(notificationId: String) {
        viewModelScope.launch {
            repository.deleteByNotificationId(notificationId)
        }
    }


    fun deleteAllMission() {
        viewModelScope.launch {
            repository.deleteAllMissions()
        }
    }

    fun deleteByHistoryMissionId(historyMissionId: String) {
        viewModelScope.launch {
            repository.deleteByHistoryMissionId(historyMissionId)
        }
    }

    fun addHistorySurvey(historySurvey: RoomHistorySurvey) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHistorySurvey(historySurvey)
        }
    }

    fun deleteAllHistorySurvey() {
        viewModelScope.launch {
            repository.deleteAllHistorySurveys()
        }
    }

    fun deleteByHistorySurveyId(historySurveyId: String) {
        viewModelScope.launch {
            repository.deleteByHistorySurveyId(historySurveyId)
        }
    }

    fun deleteByMissionId(missionId: String) {
        viewModelScope.launch {
            repository.deleteByMissionId(missionId)
        }
    }

    fun addOngoingMission(ongoingMission: RoomOngoingMission) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOngoingMission(ongoingMission)
        }
    }

    fun deleteAllOngoingMission() {
        viewModelScope.launch {
            repository.deleteAllOngoingMissions()
        }
    }

    fun deleteByOngoingMissionId(missionId: String) {
        viewModelScope.launch {
            repository.deleteByOngoingMissionId(missionId)
        }
    }

    fun readAgent(){
        viewModelScope.launch {
            _agentObjectA.postValue(repository.readAgentA())
        }
    }

    fun deleteAllAgentDetails(){
        viewModelScope.launch {
            repository.deleteAllAgentDetails()
        }
    }

    fun addSurvey(survey: RoomSurvey){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSurvey(survey)
        }
    }


    fun deleteAllSurveys(){
        viewModelScope.launch {
            repository.deleteAllSurveys()
        }
    }

    fun deleteBySurveyId(surveyId: String) {
        viewModelScope.launch {
            repository.deleteBySurveyId(surveyId)
        }
    }


}