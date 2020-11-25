package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.data.AgentData
import com.trapezoidlimited.groundforce.model.*
import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import com.trapezoidlimited.groundforce.model.LoginResponse
import com.trapezoidlimited.groundforce.model.mission.LoginRequest
import retrofit2.http.Body

/**
 * manages api queries to the network endpoints */

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass>
    suspend fun forgotPassword (email: String): Resource<ForgotPasswordResponse>
    suspend fun verifyPhone(phone: VerifyPhone): Resource<GenericResponseClass>
    suspend fun confirmPhone(confirmPhone: ConfirmPhone): Resource<GenericResponseClass>
    suspend fun registerAgent(agent: AgentData): Resource<GenericResponseClass>
}