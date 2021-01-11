package com.trapezoidlimited.groundforce.repository

import android.app.Activity
import android.net.Uri
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
import com.trapezoidlimited.groundforce.utils.EMAIL
import com.trapezoidlimited.groundforce.utils.USERID
import com.trapezoidlimited.groundforce.utils.getRealPathFromUri
import com.trapezoidlimited.groundforce.utils.loadFromSharedPreference
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * implements the AuthRepository Interface and overrides the methods to make apiService call */

class AuthRepositoryImpl
@Inject
constructor(
    private val apiService: ApiService,
    private val missionsApi: MissionsApi
) : BaseRepository(), AuthRepository {

    /** Auth Requests */

    override suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>> =
        safeApiCall {
            apiService.login(loginRequest)
        }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Resource<GenericResponseClass<ForgotPasswordResponse>>
    = safeApiCall {
        apiService.forgotPassword(forgotPasswordRequest)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Resource<GenericResponseClass<ResetPasswordResponse>>
    = safeApiCall {
        apiService.resetPassword(resetPasswordRequest)
    }

    override suspend fun verifyPhone(phone: VerifyPhoneRequest): Resource<GenericResponseClass<VerifyPhoneResponse>> =
        safeApiCall {
            apiService.verifyPhone(phone)
        }

    override suspend fun confirmPhone(confirmPhone: ConfirmPhoneRequest): Resource<GenericResponseClass<ConfirmOtpResponse>> =
        safeApiCall {
            apiService.confirmPhone(confirmPhone)
        }

    override suspend fun registerAgent(agent: AgentDataRequest): Resource<GenericResponseClass<AgentDataResponse>> =
        safeApiCall {
            apiService.registerUser(agent)
        }

    override suspend fun verifyEmail(email: VerifyEmailAddressRequest): Resource<GenericResponseClass<VerifyEmailResponse>> =
        safeApiCall {
            apiService.verifyEmail(email)
        }

    override suspend fun confirmEmail(confirmEmailAddressRequest: ConfirmEmailAddressRequest):
            Resource<GenericResponseClass<ConfirmEmailResponse>> = safeApiCall {
        apiService.confirmEmail(confirmEmailAddressRequest)
    }

    /** User Requests */


    override suspend fun getUser(
        token: String,
        id: String
    ): Resource<GenericResponseClass<UserResponse>> =
        safeApiCall {
            apiService.getUser(token, id)
        }

    override suspend fun putUser(user: PutUserRequest): Resource<GenericResponseClass<PutUserResponse>> =
        safeApiCall {
            apiService.putUser(user)
        }

    override suspend fun verifyAccount(
        token: String,
        verifyAccountRequest: VerifyAccountRequest
    ):
            Resource<GenericResponseClass<VerifyAccountResponse>> = safeApiCall {
        apiService.verifyAccount(token, verifyAccountRequest)
    }

    override suspend fun changePassword( token: String, changePasswordRequest: ChangePasswordRequest)
            : Resource<GenericResponseClass<ChangePasswordResponse>> = safeApiCall {
        apiService.changePassword(token, changePasswordRequest)
    }

    override suspend fun verifyLocation(
        token: String,
        verifyLocationRequest: VerifyLocationRequest
    ): Resource<GenericResponseClass<VerifyLocationResponse>> = safeApiCall {
        apiService.verifyLocation(token, verifyLocationRequest)
    }

    /** Mission Requests */

    override suspend fun
            getMissions(token: String, agentId: String, status: String, page: String):
            Resource<GenericResponseClass<GetMissionResponse>> = safeApiCall {
        missionsApi.getMissions(token, agentId, status, page)
    }

    override suspend fun
            updateMissionStatus(token: String, missionId: String, status: String):
            Resource<GenericResponseClass<UpdateMissionStatusResponse>> = safeApiCall {
        missionsApi.updateMissionStatus(token, missionId, status)
    }

    override suspend fun submitMission(token: String, submitMissionRequest: SubmitMissionRequest):
            Resource<GenericResponseClass<SubmitMissionResponse>> = safeApiCall {
        missionsApi.submitMission(token, submitMissionRequest)
    }

    override suspend fun getBuildingType(
        token: String,
        page: Int
    ): Resource<GenericResponseClass<GetBuildingTypeResponse>> = safeApiCall {
        missionsApi.getBuildingType(token, page)
    }

    /** Survey Requests */
    override suspend fun submitMission(submitMissionRequest: SubmitMissionRequest): Resource<GenericResponseClass<SubmitMissionResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        photoPath: Uri,
        token: String,
        activity: Activity
    ): Resource<GenericResponseClass<UploadPictureResponse>> = safeApiCall {

        val userId = loadFromSharedPreference(activity, USERID)

        val file = File(getRealPathFromUri(activity, photoPath)!!)

        val requestFile: RequestBody = file
            .asRequestBody(activity.contentResolver.getType(photoPath)?.toMediaTypeOrNull())

        val reqBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("Id", userId)
            .addFormDataPart("Photo", file.name, requestFile)
            .build()

        apiService.updatePicture(token, reqBody)
    }


    override suspend fun updateSurveyStatus(
        token: String,
        updateSurveyStatusRequest: UpdateSurveyStatusRequest
    ): Resource<GenericResponseClass<UpdateSurveyStatusResponse>> = safeApiCall{
        apiService.updateSurveyStatus(token, updateSurveyStatusRequest)
    }

    override suspend fun submitSurvey(
        token: String,
        submitSurveyRequest: SubmitSurveyRequest
    ): Resource<GenericResponseClass<SubmitSurveyResponse>> = safeApiCall {
        apiService.submitSurvey(token, submitSurveyRequest)
    }

    override suspend fun getSurvey(
        token: String,
        agentId: String,
        status: String,
        page: Int
    ): Resource<GenericResponseClass<GetSurveyResponse>> = safeApiCall{
        apiService.getSurvey(token, agentId, status, page)
    }

    override suspend fun getSurveyById(
        token: String,
        surveyId: String
    ): Resource<GenericResponseClass<GetSurveyByIDResponse>> = safeApiCall {
        apiService.getSurveyById(token, surveyId)
    }

    override suspend fun getQuestionById(
        token: String,
        questionId: String
    ): Resource<GenericResponseClass<GetQuestionByIDResponse>> = safeApiCall {
        apiService.getQuestionById(token, questionId)
    }

    override suspend fun getNotificationsByUserId(
        token: String,
        userId: String,
        page: String
    ): Resource<GenericResponseClass<GetNotificationsResponse>> = safeApiCall {
        apiService.getNotificationsByUserId(token, userId, page)
    }

    override suspend fun getAllNotifications(
        token: String,
        page: Int
    ): Resource<GenericResponseClass<GetAllNotificationsResponse>> = safeApiCall {
        apiService.getAllNotifications(token, page)
    }

}