package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import retrofit2.http.Field

/**
 * manages api queries to the network endpoints */

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>>
    suspend fun forgotPassword(email: String): Resource<ForgotPasswordResponse>
    suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>>
    suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>>
    suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>>
    suspend fun getUser(id: String): Resource<GenericResponseClass<UserResponse>>
}