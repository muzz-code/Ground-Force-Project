package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
@Inject
constructor(
    private val api: LoginAuthApi,
    private val missionsApi: MissionsApi
) : BaseRepository(), AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>> =
        safeApiCall {
            api.login(loginRequest)
        }

    override suspend fun forgotPassword(email: String) = safeApiCall {
        api.forgotPassword(email)
    }

    override suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>> =
        safeApiCall {
            api.verifyPhone(phone)
        }

    override suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>> =
        safeApiCall {
            api.confirmPhone(confirmPhone)
        }

    override suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>> =
        safeApiCall {
            api.registerUser(agent)
        }

    override suspend fun verifyEmail(email: String): Resource<GenericResponseClass<VerifyEmailResponse>> = safeApiCall {
        api.verifyEmail(email)
    }

    override suspend fun confirmEmail(email: String, verificationCode: String):
            Resource<GenericResponseClass<ConfirmEmailResponse>> = safeApiCall {
        api.confirmEmail(email, verificationCode)
    }

    override suspend fun getUser(id: String): Resource<GenericResponseClass<UserResponse>> =
        safeApiCall {
            api.getUser(id)
        }


    override suspend fun getUser(token: String, id: String): Resource<GenericResponseClass<UserResponse>> =
        safeApiCall {
            api.getUser(token, id)
        }

    override suspend fun putUser(user: PutUserRequest): Resource<GenericResponseClass<PutUserResponse>> =
        safeApiCall {
            api.putUser(user)
        }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest)
            : Resource<GenericResponseClass<ChangePasswordResponse>> = safeApiCall {
        api.changePassword(changePasswordRequest)
    }

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


}