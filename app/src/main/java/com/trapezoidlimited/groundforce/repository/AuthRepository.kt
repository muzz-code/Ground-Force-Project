package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.*


/**
 * manages api queries to the network endpoints */

interface AuthRepository {
    suspend fun login(email: String, pin: String): Resource<LoginResponse>
    suspend fun forgotPassword (email: String): Resource<ForgotPasswordResponse>
    suspend fun verifyPhone(phone: VerifyPhone): Resource<GenericResponseClass>
    suspend fun confirmPhone(confirmPhone: ConfirmPhone): Resource<GenericResponseClass>
}