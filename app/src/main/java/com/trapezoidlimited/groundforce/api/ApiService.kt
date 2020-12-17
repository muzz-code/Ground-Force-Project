package com.trapezoidlimited.groundforce.api


import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.ParentResponse
import com.trapezoidlimited.groundforce.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Query to make a network call to the login endpoint */

interface ApiService {

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
    suspend fun registerUser(
        @Body agent: AgentDataRequest
    ): GenericResponseClass<AgentDataResponse>

    @GET("User/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): GenericResponseClass<UserResponse>

    @GET("User/{id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): GenericResponseClass<UserResponse>

    @PUT("User")
    suspend fun putUser(
        @Body user: PutUserRequest
    ): GenericResponseClass<PutUserResponse>

    @PATCH("User/change-password")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): GenericResponseClass<ChangePasswordResponse>

    @Multipart
    @POST("Auth/verify-email")
    suspend fun verifyEmail(
        @Part email: String
    ): GenericResponseClass<VerifyEmailResponse>

    @Multipart
    @POST("Auth/verify-email")
    suspend fun confirmEmail(
        @Part email: String,
        @Part verificationCode: String
    ): GenericResponseClass<ConfirmEmailResponse>

    @PATCH("User/picture")
    suspend fun updatePicture(
        @Header("Authorization") token: String,
        @Body Photo: RequestBody
    ): GenericResponseClass<UploadPictureResponse>

    @PATCH("User/verify-account")
    suspend fun verifyAccount(
        @Header("Authorization") token: String,
        @Body verifyAccountRequest: VerifyAccountRequest
    ): GenericResponseClass<VerifyAccountResponse>

    @PATCH("Auth/verify-location")
    suspend fun verifyLocation(
        @Header("Authorization") token: String,
        @Body verifyLocationRequest: VerifyLocationRequest
    ): GenericResponseClass<VerifyLocationResponse>


    /**Survey Endpoints*/

    @PATCH("Survey/acceptance-status")
    suspend fun updateSurveyStatus(
        @Header("Authorization") token: String,
        @Body updateSurveyStatusRequest: UpdateSurveyStatusRequest
    ): GenericResponseClass<UpdateMissionStatusResponse>

    @POST("Survey/submit-survey")
    suspend fun submitSurvey(
        @Header("Authorization") token: String,
        @Body submitSurveyRequest: SubmitSurveyRequest
    ): GenericResponseClass<UpdateMissionStatusResponse>

    @GET("Survey/{agentId}/{status}/{page}")
    suspend fun getSurvey(
        @Header("Authorization") token: String,
        @Path("agentId") agentId: String,
        @Path("status") status: String,
        @Path("page") page: String
    ): GenericResponseClass<UpdateMissionStatusResponse>

    /**Notifications Endpoints*/

    @GET("Notification/{userId}/notifications/{page}")
    suspend fun getNotificationsByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("page") page: String
    ): GenericResponseClass<GetNotificationsResponse>

}
