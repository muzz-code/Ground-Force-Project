package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository) : ViewModel() {


    var agentObject: LiveData<List<RoomAgent>> = repository.readAgent()

    val additionalDetail: LiveData<List<RoomAdditionalDetail>> = repository.readAdditionalDetail()


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

}