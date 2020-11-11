package com.trapezoidlimited.groundforce.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.api.VerifyPhone
import com.trapezoidlimited.groundforce.api.VerifyPhoneResponse
import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import com.trapezoidlimited.groundforce.model.LoginResponse
import com.trapezoidlimited.groundforce.repository.AuthRepository
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import kotlinx.coroutines.launch


/**
 * LoginAuthViewModel class launches methods in the AuthRepository to make network calls
 * in the background and exposes loginResponse LiveData to be observed in the login fragment
 * to authorize user as appropriate. */


class LoginAuthViewModel
constructor(
    private val repository: AuthRepositoryImpl,
) : ViewModel() {

    private val _phoneVerifyResponse: MutableLiveData<Resource<VerifyPhoneResponse>> =
        MutableLiveData()
    val phoneVerifyResponse: LiveData<Resource<VerifyPhoneResponse>>
        get() = _phoneVerifyResponse

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse


    /** forgot password mutable live data*/
    private val _forgotPasswordResponse: MutableLiveData<Resource<ForgotPasswordResponse>> =
        MutableLiveData()

    /** forgot password live data */
    val forgotPasswordResponse: LiveData<Resource<ForgotPasswordResponse>>
        get() = _forgotPasswordResponse

    /** launch coroutine in viewModel scope for forgot password */
//    fun forgotPassword (email : String) = viewModelScope.launch {
//        _forgotPasswordResponse.value = repository.forgotPassword(email)
//    }

    /** launch coroutine in viewModel scope for login */
    fun login(email: String, pin: String) = viewModelScope.launch {
        _loginResponse.value = repository.login(email, pin)
    }

    fun verifyPhone(phone: VerifyPhone) = viewModelScope.launch {
        _phoneVerifyResponse.value = repository.verifyPhone(phone)
    }


}