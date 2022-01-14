package com.trapezoidlimited.groundforce.viewmodel

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.SessionManager
import com.trapezoidlimited.groundforce.utils.TOKEN
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Path
import java.io.File

/**
 * AuthViewModel class launches methods in the AuthRepository to make network calls
 * in the background and exposes loginResponse LiveData to be observed in the login fragment
 * to authorize user as appropriate. */

class AuthViewModel(
    private val repository: AuthRepositoryImpl,
    private val context: Context
) : ViewModel() {

    val token = "Bearer ${SessionManager.load(context, TOKEN)}"


    /** Auth Responses */

    private val _loginResponse: MutableLiveData<Resource<GenericResponseClass<LoginResponse>>> =
        MutableLiveData()
    val loginResponse: LiveData<Resource<GenericResponseClass<LoginResponse>>>
        get() = _loginResponse


    private val _forgotPasswordResponse: MutableLiveData<Resource<GenericResponseClass<ForgotPasswordResponse>>> =
        MutableLiveData()

    val forgotPasswordResponse: LiveData<Resource<GenericResponseClass<ForgotPasswordResponse>>>
        get() = _forgotPasswordResponse

    private val _resetPasswordResponse: MutableLiveData<Resource<GenericResponseClass<ResetPasswordResponse>>> =
        MutableLiveData()
    val resetPasswordResponse: LiveData<Resource<GenericResponseClass<ResetPasswordResponse>>>
        get() = _resetPasswordResponse


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
    private val _getUserResponse: MutableLiveData<Resource<GenericResponseClass<UserResponse>>> =
        MutableLiveData()
    val getUserResponse: LiveData<Resource<GenericResponseClass<UserResponse>>>
        get() = _getUserResponse


    /** change password response */
    private val _changePasswordResponse: MutableLiveData<Resource<GenericResponseClass<ChangePasswordResponse>>> =
        MutableLiveData()
    val changePasswordResponse: LiveData<Resource<GenericResponseClass<ChangePasswordResponse>>>
        get() = _changePasswordResponse

    private val _verifyEmailResponse: MutableLiveData<Resource<GenericResponseClass<VerifyEmailResponse>>> =
        MutableLiveData()
    val verifyEmailResponse: LiveData<Resource<GenericResponseClass<VerifyEmailResponse>>>
        get() = _verifyEmailResponse

    private val _confirmEmailResponse: MutableLiveData<Resource<GenericResponseClass<ConfirmEmailResponse>>> =
        MutableLiveData()
    val confirmEmailResponse: LiveData<Resource<GenericResponseClass<ConfirmEmailResponse>>>
        get() = _confirmEmailResponse


    /** User responses */

    private val _getUserDetailsResponse: MutableLiveData<Resource<GenericResponseClass<UserResponse>>> =
        MutableLiveData()
    val getUserDetailsResponse: LiveData<Resource<GenericResponseClass<UserResponse>>>
        get() = _getUserDetailsResponse

    private val _putUserResponse: MutableLiveData<Resource<GenericResponseClass<PutUserResponse>>> =
        MutableLiveData()
    val putUserResponse: LiveData<Resource<GenericResponseClass<PutUserResponse>>>
        get() = _putUserResponse

    private val _verifyLocationResponse: MutableLiveData<Resource<GenericResponseClass<VerifyLocationResponse>>> =
        MutableLiveData()
    val verifyLocationResponse: LiveData<Resource<GenericResponseClass<VerifyLocationResponse>>>
        get() = _verifyLocationResponse

    /** response for agent creation */
    val _agentCreationResponse: MutableLiveData<Resource<GenericResponseClass<AgentDataResponse>>> =
        MutableLiveData()
    val agentCreationResponse: LiveData<Resource<GenericResponseClass<AgentDataResponse>>>
        get() = _agentCreationResponse

    private val _verifyAccountResponse: MutableLiveData<Resource<GenericResponseClass<VerifyAccountResponse>>> =
        MutableLiveData()
    val verifyAccountResponse: LiveData<Resource<GenericResponseClass<VerifyAccountResponse>>>
        get() = _verifyAccountResponse


    /** Survey responses */

    private val _updateSurveyStatusResponse: MutableLiveData<Resource<GenericResponseClass<UpdateSurveyStatusResponse>>> =
        MutableLiveData()
    val updateSurveyStatusResponse: MutableLiveData<Resource<GenericResponseClass<UpdateSurveyStatusResponse>>>
        get() = _updateSurveyStatusResponse

    private val _submitSurveyResponse: MutableLiveData<Resource<GenericResponseClass<SubmitSurveyResponse>>> =
        MutableLiveData()
    val submitSurveyResponse: MutableLiveData<Resource<GenericResponseClass<SubmitSurveyResponse>>>
        get() = _submitSurveyResponse

    private val _getSurveyByIDResponse: MutableLiveData<Resource<GenericResponseClass<GetSurveyByIDResponse>>> =
        MutableLiveData()
    val getSurveyByIDResponse: MutableLiveData<Resource<GenericResponseClass<GetSurveyByIDResponse>>>
        get() = _getSurveyByIDResponse

    private val _getQuestionByIDResponse: MutableLiveData<Resource<GenericResponseClass<GetQuestionByIDResponse>>> =
        MutableLiveData()
    val getQuestionByIDResponse: MutableLiveData<Resource<GenericResponseClass<GetQuestionByIDResponse>>>
        get() = _getQuestionByIDResponse


    /** Image Url Response **/
    private val _imageUrl: MutableLiveData<Resource<GenericResponseClass<UploadPictureResponse>>> =
        MutableLiveData()
    val imageUrl: LiveData<Resource<GenericResponseClass<UploadPictureResponse>>>
        get() = _imageUrl

    private val _getSurveyResponse: MutableLiveData<Resource<GenericResponseClass<GetSurveyResponse>>> =
        MutableLiveData()
    val getSurveyResponse: LiveData<Resource<GenericResponseClass<GetSurveyResponse>>>
        get() = _getSurveyResponse


    private val _getNotificationsResponse: MutableLiveData<Resource<GenericResponseClass<GetNotificationsResponse>>> =
        MutableLiveData()
    val getNotificationsResponse: MutableLiveData<Resource<GenericResponseClass<GetNotificationsResponse>>>
        get() = _getNotificationsResponse

    private val _getAllNotificationsResponse: MutableLiveData<Resource<GenericResponseClass<GetAllNotificationsResponse>>> =
        MutableLiveData()
    val getAllNotificationsResponse: MutableLiveData<Resource<GenericResponseClass<GetAllNotificationsResponse>>>
        get() = _getAllNotificationsResponse


    /** Auth Requests */

    fun login(loginRequest: LoginRequest) = viewModelScope.launch {
        _loginResponse.value = repository.login(loginRequest)
    }

    fun verifyPhone(phone: VerifyPhoneRequest) = viewModelScope.launch {
        _verifyPhoneResponse.value = repository.verifyPhone(phone)
    }

    fun confirmPhone(confirmPhone: ConfirmPhoneRequest) = viewModelScope.launch {
        _confirmPhoneResponse.value = repository.confirmPhone(confirmPhone)
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest) = viewModelScope.launch {
        _changePasswordResponse.value = repository.changePassword(token, changePasswordRequest)
    }

    fun verifyEmail(email: VerifyEmailAddressRequest) = viewModelScope.launch {
        _verifyEmailResponse.value = repository.verifyEmail(email)
    }

    fun confirmEmail(confirmEmailAddressRequest: ConfirmEmailAddressRequest) =
        viewModelScope.launch {
            _confirmEmailResponse.value = repository.confirmEmail(confirmEmailAddressRequest)
        }

    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) = viewModelScope.launch {
        _forgotPasswordResponse.value = repository.forgotPassword(forgotPasswordRequest)
    }

    fun resetPassword(resetPasswordRequest: ResetPasswordRequest) = viewModelScope.launch {
        _resetPasswordResponse.value = repository.resetPassword(resetPasswordRequest)
    }


    /** User Requests */

    fun registerAgent(agent: AgentDataRequest) = viewModelScope.launch {
        _agentCreationResponse.value = repository.registerAgent(agent)
    }


    fun getUser(id: String) = viewModelScope.launch {
        _getUserDetailsResponse.value = repository.getUser(token, id)
    }

    fun putUser(user: PutUserRequest) = viewModelScope.launch {
        _putUserResponse.value = repository.putUser(token, user)
    }


    fun verifyLocation(verifyLocationRequest: VerifyLocationRequest) = viewModelScope.launch {
        _verifyLocationResponse.value = repository.verifyLocation(token, verifyLocationRequest)
    }

    fun verifyAccount(verifyAccountRequest: VerifyAccountRequest) = viewModelScope.launch {
        _verifyAccountResponse.value = repository.verifyAccount(token, verifyAccountRequest)
    }


    /** Survey Requests */
    fun updateSurveyStatus(updateSurveyStatusRequest: UpdateSurveyStatusRequest) =
        viewModelScope.launch {
            _updateSurveyStatusResponse.value =
                repository.updateSurveyStatus(token, updateSurveyStatusRequest)
        }

    fun submitSurvey(submitSurveyRequest: SubmitSurveyRequest) = viewModelScope.launch {
        _submitSurveyResponse.value = repository.submitSurvey(token, submitSurveyRequest)

    }


    fun getSurvey(agentId: String, status: String, page: Int) = viewModelScope.launch {
        _getSurveyResponse.value = repository.getSurvey(token, agentId, status, page)
    }

    fun getSurveyById(
        surveyId: String,
    ) = viewModelScope.launch {
        _getSurveyByIDResponse.value = repository.getSurveyById(token, surveyId)
    }


    fun getNotificationsByUserId(
        userId: String,
        page: String
    ) = viewModelScope.launch {
        _getNotificationsResponse.value =
            repository.getNotificationsByUserId(token, userId, page)
    }


    fun uploadImage(
        photoPath: Uri,
        activity: Activity
    ) = viewModelScope.launch {

        _imageUrl.value = repository.uploadImage(photoPath, token, activity)
    }

    fun getAllNotifications(
        page: Int
    ) = viewModelScope.launch {
        _getAllNotificationsResponse.value = repository.getAllNotifications(token, page)
    }

    fun getQuestionById(
        questionId: String,
    ) = viewModelScope.launch {
        _getQuestionByIDResponse.value = repository.getQuestionById(token, questionId)
    }


}