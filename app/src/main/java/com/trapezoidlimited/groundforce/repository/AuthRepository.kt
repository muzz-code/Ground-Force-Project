package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import com.trapezoidlimited.groundforce.model.LoginResponse
import retrofit2.http.Field

/**
 * manages api queries to the network endpoints */

interface AuthRepository {
    suspend fun login(email: String, pin: String): Resource<LoginResponse>
    suspend fun forgotPassword (email: String): Resource<ForgotPasswordResponse>
}