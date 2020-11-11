package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.model.*
import retrofit2.http.*

/**
 * Query to make a network call to the login endpoint */

interface LoginAuthApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("pin") pin: String
    ): LoginResponse


    /**
     * Query to make a network call to the forgot endpoint */
    @FormUrlEncoded
    @POST("endpoint")
    suspend fun forgotPassword (
        @Field("email") email: String
    ): ForgotPasswordResponse


    @POST("Auth/verify-phone")
    suspend fun verifyPhone(
        @Body phone: VerifyPhone
    ): GenericResponseClass

    @POST("Auth/confirm-otp")
    suspend fun confirmPhone(
        @Body confirmPhone: ConfirmPhone
    ): GenericResponseClass

//    @GET
//    suspend fun profile(
//        @Header("Authorization") header: String
//    ): LoginResponse


}