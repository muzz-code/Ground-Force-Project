package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.model.ForgotPasswordResponse
import com.trapezoidlimited.groundforce.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ForgotPasswordApi {

    @FormUrlEncoded
    @POST("endpoint")
    suspend fun submitEmail (
        @Field("email") email: String
    ): ForgotPasswordResponse
}