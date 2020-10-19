package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.ForgotPasswordApi
import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
@Inject
constructor(
    private val api: LoginAuthApi,
    private val forgotPasswordApi: ForgotPasswordApi
) : BaseRepository(), AuthRepository {


    override suspend fun login(email: String, pin: String) = safeApiCall {
        api.login(email, pin)
    }

    override suspend fun submitEmail(email: String)= safeApiCall{
        forgotPasswordApi.submitEmail(email)
    }



}