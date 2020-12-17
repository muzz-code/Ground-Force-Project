package com.trapezoidlimited.groundforce.repository

import android.app.Activity
import android.net.Uri
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.*
import com.trapezoidlimited.groundforce.model.response.*
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

    override suspend fun login(loginRequest: LoginRequest): Resource<GenericResponseClass<LoginResponse>> =
        safeApiCall {
            apiService.login(loginRequest)
        }

    override suspend fun forgotPassword(email: String) = safeApiCall {
        apiService.forgotPassword(email)
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

    override suspend fun verifyEmail(email: String): Resource<GenericResponseClass<VerifyEmailResponse>> =
        safeApiCall {
            apiService.verifyEmail(email)
        }

    override suspend fun confirmEmail(email: String, verificationCode: String):
            Resource<GenericResponseClass<ConfirmEmailResponse>> = safeApiCall {
        apiService.confirmEmail(email, verificationCode)
    }

    override suspend fun getUser(id: String): Resource<GenericResponseClass<UserResponse>> =
        safeApiCall {
            apiService.getUser(id)
        }


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

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest)
            : Resource<GenericResponseClass<ChangePasswordResponse>> = safeApiCall {
        apiService.changePassword(changePasswordRequest)
    }

    override suspend fun verifyLocation(
        token: String,
        verifyLocationRequest: VerifyLocationRequest
    ): Resource<GenericResponseClass<VerifyLocationResponse>> = safeApiCall {
        apiService.verifyLocation(token, verifyLocationRequest)
    }

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


}