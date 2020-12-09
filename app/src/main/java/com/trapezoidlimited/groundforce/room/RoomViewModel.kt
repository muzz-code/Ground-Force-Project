package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trapezoidlimited.groundforce.data.AgentObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository) : ViewModel() {


    var agentObject: LiveData<List<RoomAgent>> =  repository.readAgent()

    val additionalDetail: LiveData<List<RoomAdditionalDetail>> = repository.readAdditionalDetail()

    var mission: LiveData<List<RoomMission>> = repository.readAllMissions()

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

    fun deleteAllMission() {
        viewModelScope.launch {
            repository.deleteAllMissions()
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

}