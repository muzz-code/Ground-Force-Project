package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
constructor(
    private val api: LoginAuthApi
) : BaseRepository(), AuthRepository {


    override suspend fun login(email: String, password: String) = safeApiCall {
        api.login(email, password)
    }



}