package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
@Inject
constructor(
    private val api: LoginAuthApi
) : BaseRepository(), AuthRepository {


    override suspend fun login(email: String, pin: String) = safeApiCall {
        api.login(email, pin)
    }

    override suspend fun forgotPassword(email: String)= safeApiCall{
        api.forgotPassword(email)
    }



}