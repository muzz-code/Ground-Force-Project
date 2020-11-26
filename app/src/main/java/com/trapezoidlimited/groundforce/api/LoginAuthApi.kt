package com.trapezoidlimited.groundforce.api


import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.ParentResponse
import com.trapezoidlimited.groundforce.model.response.*
import retrofit2.http.*

/**
 * Query to make a network call to the login endpoint */

interface LoginAuthApi {

    @POST("Auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): GenericResponseClass<LoginResponse>


    /**
     * Query to make a network call to the forgot endpoint */
    @FormUrlEncoded
    @POST("endpoint")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): ForgotPasswordResponse

    @POST("Auth/verify-phone")
    suspend fun verifyPhone(
        @Body phone: VerifyPhoneRequest
    ): GenericResponseClass<VerifyPhoneResponse>

    @POST("Auth/confirm-otp")
    suspend fun confirmPhone(
        @Body confirmPhone: ConfirmPhoneRequest
    ): GenericResponseClass<ConfirmOtpResponse>

    @POST("Auth/register")
    suspend fun registerAgent(
        @Body agent: AgentDataRequest
    ): GenericResponseClass<AgentDataResponse>

    @GET("User/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): GenericResponseClass<UserResponse>

}
