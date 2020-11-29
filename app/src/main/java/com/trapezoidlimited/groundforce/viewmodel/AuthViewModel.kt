package com.trapezoidlimited.groundforce.viewmodel

import androidx.lifecycle.*
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.ParentResponse
import com.trapezoidlimited.groundforce.model.response.*
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import kotlinx.coroutines.launch


/**
 * AuthViewModel class launches methods in the AuthRepository to make network calls
 * in the background and exposes loginResponse LiveData to be observed in the login fragment
 * to authorize user as appropriate. */


class AuthViewModel(
    private val repository: AuthRepositoryImpl
) : ViewModel() {

    private val _loginResponse: MutableLiveData<Resource<GenericResponseClass<LoginResponse>>> =
        MutableLiveData()
    val loginResponse: LiveData<Resource<GenericResponseClass<LoginResponse>>>
        get() = _loginResponse

    /** forgot password mutable live data*/
    private val _forgotPasswordResponse: MutableLiveData<Resource<ForgotPasswordResponse>> =
        MutableLiveData()

    /** forgot password live data */
    val forgotPasswordResponse: LiveData<Resource<ForgotPasswordResponse>>
        get() = _forgotPasswordResponse


    /** verify phone number response */

    val _verifyPhoneResponse: MutableLiveData<Resource<GenericResponseClass<VerifyPhoneResponse>>> =
        MutableLiveData()
    val verifyPhoneResponse: LiveData<Resource<GenericResponseClass<VerifyPhoneResponse>>>
        get() = _verifyPhoneResponse

    /** confirm phone number response */

    val _confirmPhoneResponse: MutableLiveData<Resource<GenericResponseClass<ConfirmOtpResponse>>> =
        MutableLiveData()
    val confirmPhoneResponse: LiveData<Resource<GenericResponseClass<ConfirmOtpResponse>>>
        get() = _confirmPhoneResponse

    /** get user details response */

    private val _getUserResponse: MutableLiveData<Resource<GenericResponseClass<UserResponse>>> = MutableLiveData()
    val getUserResponse: LiveData<Resource<GenericResponseClass<UserResponse>>>
        get() = _getUserResponse

    /** updating user details response */

    private val _putUserResponse: MutableLiveData<Resource<GenericResponseClass<PutUserResponse>>> = MutableLiveData()
    val putUserResponse: LiveData<Resource<GenericResponseClass<PutUserResponse>>>
        get() = _putUserResponse

    /** change password response */
    private val _changePasswordResponse: MutableLiveData<Resource<GenericResponseClass<ChangePasswordResponse>>> = MutableLiveData()
    val changePasswordResponse: LiveData<Resource<GenericResponseClass<ChangePasswordResponse>>>
        get() = _changePasswordResponse


    /** response for agent creation */

    val _agentCreationResponse: MutableLiveData<Resource<GenericResponseClass<AgentDataResponse>>> =
        MutableLiveData()
    val agentCreationResponse: LiveData<Resource<GenericResponseClass<AgentDataResponse>>>
        get() = _agentCreationResponse

    /** launch coroutine in viewModel scope for forgot password */
    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgotPasswordResponse.value = repository.forgotPassword(email)
    }


    /** launch coroutine in viewModel scope for login */
    fun login(loginRequest: LoginRequest) = viewModelScope.launch {
        _loginResponse.value = repository.login(loginRequest)
    }

    fun verifyPhone(phone: VerifyPhoneRequest) = viewModelScope.launch {
        _verifyPhoneResponse.value = repository.verifyPhone(phone)
    }

    fun confirmPhone(confirmPhone: ConfirmPhoneRequest) = viewModelScope.launch {
        _confirmPhoneResponse.value = repository.confirmPhone(confirmPhone)
    }

    fun registerAgent(agent: AgentDataRequest) = viewModelScope.launch {
        _agentCreationResponse.value = repository.registerAgent(agent)
    }

    fun getUser(id: String) = viewModelScope.launch {
        _getUserResponse.value = repository.getUser(id)
    }

    fun putUser(user: PutUserRequest) = viewModelScope.launch {
        _putUserResponse.value = repository.putUser(user)
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest) = viewModelScope.launch {
        _changePasswordResponse.value = repository.changePassword(changePasswordRequest)
    }

}