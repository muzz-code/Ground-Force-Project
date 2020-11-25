package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.model.request.LoginRequest
import com.trapezoidlimited.groundforce.model.request.ConfirmPhoneRequest
import com.trapezoidlimited.groundforce.model.request.VerifyPhoneRequest
import com.trapezoidlimited.groundforce.model.response.*

/**
 * manages api queries to the network endpoints */

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>>
    suspend fun forgotPassword(email: String): Resource<ForgotPasswordResponse>
    suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>>
    suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>>
    suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>>
}