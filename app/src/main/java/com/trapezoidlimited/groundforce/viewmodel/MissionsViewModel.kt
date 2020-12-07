package com.trapezoidlimited.groundforce.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.SubmitMissionRequest
import com.trapezoidlimited.groundforce.model.response.GenericResponseClass
import com.trapezoidlimited.groundforce.model.response.GetMissionResponse
import com.trapezoidlimited.groundforce.model.response.SubmitMissionResponse
import com.trapezoidlimited.groundforce.model.response.UpdateMissionStatusResponse
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.SessionManager
import com.trapezoidlimited.groundforce.utils.TOKEN
import kotlinx.coroutines.launch

class MissionsViewModel(
    private val repository: AuthRepositoryImpl,
    private val context: Context
) : ViewModel() {

    val token = "Bearer ${ SessionManager.load(context, TOKEN)}"

    private val _getMissionResponse: MutableLiveData<Resource<GenericResponseClass<GetMissionResponse>>> = MutableLiveData()
    val getMissionResponse: LiveData<Resource<GenericResponseClass<GetMissionResponse>>>
        get() = _getMissionResponse

    private val _updateMissionStatusResponse: MutableLiveData<Resource<GenericResponseClass<UpdateMissionStatusResponse>>> = MutableLiveData()
    val updateMissionStatusResponse: LiveData<Resource<GenericResponseClass<UpdateMissionStatusResponse>>>
        get() = _updateMissionStatusResponse

    private val _submitMissionResponse: MutableLiveData<Resource<GenericResponseClass<SubmitMissionResponse>>> = MutableLiveData()
    val submitMissionResponse: LiveData<Resource<GenericResponseClass<SubmitMissionResponse>>>
        get() = _submitMissionResponse

    fun getMissions(
                    agentId: String,
                    status: String,
                    page: String) = viewModelScope.launch {
        _getMissionResponse.value = repository.getMissions(token, agentId, status, page)
    }

    fun updateMissionStatus(
        missionId: String,
        status: String
    ) = viewModelScope.launch {
        _updateMissionStatusResponse.value = repository.updateMissionStatus(token, missionId, status)
    }

    fun submitMission(
        submitMissionRequest: SubmitMissionRequest
    ) = viewModelScope.launch {
        _submitMissionResponse.value = repository.submitMission(token, submitMissionRequest)
    }


}