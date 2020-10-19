package com.trapezoidlimited.groundforce.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import com.trapezoidlimited.groundforce.repository.AuthRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel @ViewModelInject constructor(
    private val repository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _forgotPasswordResponse : MutableLiveData<Resource<ForgotPasswordResponse>> = MutableLiveData()

    val forgotPasswordResponse : LiveData<Resource<ForgotPasswordResponse>>
        get() = _forgotPasswordResponse

    fun submitEmail (email : String) = viewModelScope.launch {
        _forgotPasswordResponse.value = repository.submitEmail(email)
    }

}