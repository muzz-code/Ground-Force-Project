package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import retrofit2.http.Header
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make apiService call */

class AuthRepositoryImpl
@Inject
constructor(
    private val apiService: ApiService,
    private val missionsApi: MissionsApi
) : BaseRepository(), AuthRepository {

    /** Auth Requests */

    override suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>> =
        safeApiCall {
            apiService.login(loginRequest)
        }

    override suspend fun forgotPassword(email: String) = safeApiCall {
        apiService.forgotPassword(email)
    }

    override suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>> =
        safeApiCall {
            apiService.verifyPhone(phone)
        }

    override suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>> =
        safeApiCall {
            apiService.confirmPhone(confirmPhone)
        }

    override suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>> =
        safeApiCall {
            apiService.registerUser(agent)
        }

    override suspend fun verifyEmail(email: String): Resource<GenericResponseClass<VerifyEmailResponse>> = safeApiCall {
        apiService.verifyEmail(email)
    }

    override suspend fun confirmEmail(email: String, verificationCode: String):
            Resource<GenericResponseClass<ConfirmEmailResponse>> = safeApiCall {
        apiService.confirmEmail(email, verificationCode)
    }

    /** User Requests */


    override suspend fun getUser(token: String, id: String): Resource<GenericResponseClass<UserResponse>> =
        safeApiCall {
            apiService.getUser(token, id)
        }

    override suspend fun putUser(user: PutUserRequest): Resource<GenericResponseClass<PutUserResponse>> =
        safeApiCall {
            apiService.putUser(user)
        }


    override suspend fun verifyAccount(token: String,
        verifyAccountRequest: VerifyAccountRequest):
            Resource<GenericResponseClass<VerifyAccountResponse>> = safeApiCall {
        apiService.verifyAccount(token, verifyAccountRequest)
    }

    override suspend fun changePassword( token: String, changePasswordRequest: ChangePasswordRequest)
            : Resource<GenericResponseClass<ChangePasswordResponse>> = safeApiCall {
        apiService.changePassword(token, changePasswordRequest)
    }

    override suspend fun verifyLocation(
        token: String,
        verifyLocationRequest: VerifyLocationRequest
    ): Resource<GenericResponseClass<VerifyLocationResponse>> = safeApiCall {
        apiService.verifyLocation(token, verifyLocationRequest)
    }

    /** Mission Requests */

    override suspend fun
            getMissions(token: String, agentId: String, status: String, page: String):
                Resource<GenericResponseClass<GetMissionResponse>> = safeApiCall {
                        missionsApi.getMissions(token, agentId, status, page)
                }

    override suspend fun
            updateMissionStatus(token: String, missionId: String, status: String):
            Resource<GenericResponseClass<UpdateMissionStatusResponse>> = safeApiCall {
                missionsApi.updateMissionStatus(token, missionId, status)
            }

    override suspend fun submitMission(token: String, submitMissionRequest: SubmitMissionRequest):
            Resource<GenericResponseClass<SubmitMissionResponse>> = safeApiCall{
        missionsApi.submitMission(token, submitMissionRequest)
    }

    override suspend fun getBuildingType(
        token: String,
        page: Int
    ): Resource<GenericResponseClass<GetBuildingTypeResponse>> = safeApiCall {
        missionsApi.getBuildingType(token, page)
    }

    /** Survey Requests */

    override suspend fun updateSurveyStatus(
        token: String,
        updateSurveyStatusRequest: UpdateSurveyStatusRequest
    ): Resource<GenericResponseClass<UpdateSurveyStatusResponse>> = safeApiCall{
        apiService.updateSurveyStatus(token, updateSurveyStatusRequest)
    }

    override suspend fun submitSurvey(
        token: String,
        submitSurveyRequest: SubmitSurveyRequest
    ): Resource<GenericResponseClass<SubmitSurveyResponse>> = safeApiCall {
        apiService.submitSurvey(token, submitSurveyRequest)
    }

    override suspend fun getSurvey(
        token: String,
        agentId: String,
        status: String,
        page: String
    ): Resource<GenericResponseClass<GetSurveyResponse>> = safeApiCall{
        apiService.getSurvey(token, agentId, status, page)
    }

    override suspend fun getNotificationsByUserId(
        token: String,
        userId: String,
        page: String
    ): Resource<GenericResponseClass<GetNotificationsResponse>> = safeApiCall {
        apiService.getNotificationsByUserId(token, userId, page)
    }
}