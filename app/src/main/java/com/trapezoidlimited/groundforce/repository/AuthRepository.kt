package com.trapezoidlimited.groundforce.repository

import android.app.Activity
import android.net.Uri
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path


/**
 * manages api queries to the network endpoints */

interface AuthRepository {

    /** Auth Requests */

    suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>>
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Resource<GenericResponseClass<ForgotPasswordResponse>>
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Resource<GenericResponseClass<ResetPasswordResponse>>
    suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>>
    suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>>
    suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>>
    suspend fun verifyEmail(email: VerifyEmailAddressRequest): Resource<GenericResponseClass<VerifyEmailResponse>>
    suspend fun confirmEmail(confirmEmailAddressRequest: ConfirmEmailAddressRequest): Resource<GenericResponseClass<ConfirmEmailResponse>>

    /** User Requests */

    suspend fun getUser(token: String, id: String): Resource<GenericResponseClass<UserResponse>>
    suspend fun putUser(token: String, user: PutUserRequest): Resource<GenericResponseClass<PutUserResponse>>
    suspend fun verifyAccount(token: String,
        verifyAccountRequest: VerifyAccountRequest): Resource<GenericResponseClass<VerifyAccountResponse>>
    suspend fun changePassword(token: String,changePasswordRequest: ChangePasswordRequest): Resource<GenericResponseClass<ChangePasswordResponse>>
    suspend fun verifyLocation(token: String,
                               verifyLocationRequest: VerifyLocationRequest): Resource<GenericResponseClass<VerifyLocationResponse>>


    /** Mission Requests */

    suspend fun getMissions(token: String, agentId: String, status: String, page: String): Resource<GenericResponseClass<GetMissionResponse>>
    suspend fun updateMissionStatus(token: String, missionId: String, status: String): Resource<GenericResponseClass<UpdateMissionStatusResponse>>
    suspend fun submitMission(
        token: String,
        submitMissionRequest: SubmitMissionRequest
    ): Resource<GenericResponseClass<SubmitMissionResponse>>

    suspend fun getBuildingType(
        token: String, page: Int
    ): Resource<GenericResponseClass<GetBuildingTypeResponse>>


    /** Survey Requests */
    suspend fun updateSurveyStatus(token: String,
                                   updateSurveyStatusRequest: UpdateSurveyStatusRequest
                                ): Resource<GenericResponseClass<UpdateSurveyStatusResponse>>

    suspend fun submitSurvey(token: String,
                            submitSurveyRequest: SubmitSurveyRequest
                        ): Resource<GenericResponseClass<SubmitSurveyResponse>>

    suspend fun getSurvey(token: String, agentId: String, status: String,
                        page: Int
                     ): Resource<GenericResponseClass<GetSurveyResponse>>

    suspend fun getSurveyById(token: String, surveyId : String,
    ): Resource<GenericResponseClass<GetSurveyByIDResponse>>

    suspend fun getQuestionById(token: String, questionId : String,
    ): Resource<GenericResponseClass<GetQuestionByIDResponse>>


    /** Notification Request */
    suspend fun getNotificationsByUserId(token: String, userId: String,
                                         page: String): Resource<GenericResponseClass<GetNotificationsResponse>>

    suspend fun getAllNotifications(token: String, page: Int
    ): Resource<GenericResponseClass<GetAllNotificationsResponse>>


    suspend fun submitMission(submitMissionRequest: SubmitMissionRequest): Resource<GenericResponseClass<SubmitMissionResponse>>


    suspend fun uploadImage(
        photoPath: Uri,
        token: String,
        activity: Activity
    ): Resource<GenericResponseClass<UploadPictureResponse>>

}