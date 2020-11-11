package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.VerifyPhone
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make api call */

class AuthRepositoryImpl
@Inject
constructor(
    private val api: LoginAuthApi
) : BaseRepository() {


    suspend fun login(email: String, pin: String) = safeApiCall {
        api.login(email, pin)
    }

    suspend fun verifyPhone(phone: VerifyPhone) = safeApiCall {
        api.verifyPhone(phone)
    }

//    override suspend fun forgotPassword(email: String)= safeApiCall{
//        api.forgotPassword(email)
//    }


}