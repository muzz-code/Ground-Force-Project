package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*

/**
 * manages api queries to the network endpoints */

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>>
    suspend fun forgotPassword(email: String): Resource<ForgotPasswordResponse>
    suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>>
    suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>>
    suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>>
    suspend fun getUser(id: String): Resource<GenericResponseClass<UserResponse>>
    suspend fun getUser(token: String, id: String): Resource<GenericResponseClass<UserResponse>>
    suspend fun putUser(user: PutUserRequest): Resource<GenericResponseClass<PutUserResponse>>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Resource<GenericResponseClass<ChangePasswordResponse>>
    suspend fun getMissions(token: String, agentId: String, status: String, page: String): Resource<GenericResponseClass<GetMissionResponse>>
    suspend fun updateMissionStatus(token: String, missionId: String, status: String): Resource<GenericResponseClass<UpdateMissionStatusResponse>>
    suspend fun submitMission(submitMissionRequest: SubmitMissionRequest): Resource<GenericResponseClass<SubmitMissionResponse>>
}