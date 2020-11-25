package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.model.request.ConfirmPhoneRequest
import com.trapezoidlimited.groundforce.model.request.VerifyPhoneRequest
import com.trapezoidlimited.groundforce.model.request.LoginRequest
import com.trapezoidlimited.groundforce.model.response.*
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
@Inject
constructor(
    private val api: LoginAuthApi
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
            api.registerAgent(agent)
        }

}