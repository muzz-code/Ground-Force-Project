package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.data.AgentData
import com.trapezoidlimited.groundforce.model.ConfirmPhone
import com.trapezoidlimited.groundforce.model.GenericResponseClass
import com.trapezoidlimited.groundforce.model.VerifyPhone
import com.trapezoidlimited.groundforce.model.mission.LoginRequest
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
@Inject
constructor(
    private val api: LoginAuthApi
) : BaseRepository(), AuthRepository {

    override suspend fun login(loginRequest: LoginRequest) = safeApiCall {
        api.login(loginRequest)
    }

    override suspend fun forgotPassword(email: String) = safeApiCall {
        api.forgotPassword(email)
    }

    override suspend fun verifyPhone(phone: VerifyPhone) = safeApiCall {
        api.verifyPhone(phone)
    }

    override suspend fun confirmPhone(confirmPhone: ConfirmPhone): Resource<GenericResponseClass> =
        safeApiCall {
            api.confirmPhone(confirmPhone)
        }

    override suspend fun registerAgent(agent: AgentData): Resource<GenericResponseClass> =
        safeApiCall {
            api.registerAgent(agent)
        }

}