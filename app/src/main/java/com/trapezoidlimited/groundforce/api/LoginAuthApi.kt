package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import com.trapezoidlimited.groundforce.model.LoginResponse
import retrofit2.http.*

/**
 * Query to make a network call to the login endpoint */

interface LoginAuthApi {

    //Retrofit Login
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @POST("https://gforceapi-heroku.herokuapp.com/api/v1/Auth/verify-phone")
    suspend fun verifyPhone(
        @Body phone: VerifyPhone
    ): VerifyPhoneResponse
}




data class VerifyPhoneResponse(
    val message: String?,
    val data: Any?
)


data class VerifyPhone(
    val phoneNumber: String
)