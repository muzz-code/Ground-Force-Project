package com.trapezoidlimited.groundforce.api


import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import com.trapezoidlimited.groundforce.model.response.UpdateSurveyStatusResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
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

    @POST("Auth/forgot-password")
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): GenericResponseClass<ForgotPasswordResponse>

    @POST("Auth/verify-phone")
    suspend fun verifyPhone(
        @Body phone: VerifyPhoneRequest
    ): GenericResponseClass<VerifyPhoneResponse>

    @POST("Auth/reset-password")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): GenericResponseClass<ResetPasswordResponse>

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
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): GenericResponseClass<ChangePasswordResponse>


    @POST("Auth/verify-email")
    suspend fun verifyEmail(
        @Body email: VerifyEmailAddressRequest
    ): GenericResponseClass<VerifyEmailResponse>


    @POST("Auth/confirm-email")
    suspend fun confirmEmail(
        @Body confirmEmailAddressRequest: ConfirmEmailAddressRequest
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
    ): GenericResponseClass<UpdateSurveyStatusResponse>

    @POST("Survey/submit-survey")
    suspend fun submitSurvey(
        @Header("Authorization") token: String,
        @Body submitSurveyRequest: SubmitSurveyRequest
    ): GenericResponseClass<SubmitSurveyResponse>

    @GET("Survey/{agentId}/{status}/{page}")
    suspend fun getSurvey(
        @Header("Authorization") token: String,
        @Path("agentId") agentId: String,
        @Path("status") status: String,
        @Path("page") page: Int
    ): GenericResponseClass<GetSurveyResponse>

    @GET("Survey/survey-questions/{surveyId}")
    suspend fun getSurveyById(
        @Header("Authorization") token: String,
        @Path("surveyId") surveyId : String,
    ): GenericResponseClass<GetSurveyByIDResponse>

    @GET("Survey/question/{questionId}")
    suspend fun getQuestionById(
        @Header("Authorization") token: String,
        @Path("questionId") questionId : String,
    ): GenericResponseClass<GetQuestionByIDResponse>



    /**Notifications Endpoints*/

    @GET("Notification/{userId}/notifications/{page}")
    suspend fun getNotificationsByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Path("page") page: String
    ): GenericResponseClass<GetNotificationsResponse>

    @GET("Notification/{page}/all-notifications")
    suspend fun getAllNotifications(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): GenericResponseClass<GetAllNotificationsResponse>

}
