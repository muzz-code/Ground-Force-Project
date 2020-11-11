package com.trapezoidlimited.groundforce.utils

import com.trapezoidlimited.groundforce.api.VerifyPhoneResponse
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class ErrorUtils
@Inject
constructor
    (var retrofit: Retrofit) {

    fun parseError(response: Response<*>): VerifyPhoneResponse {

        val converter: Converter<ResponseBody, VerifyPhoneResponse> = retrofit
            .responseBodyConverter(VerifyPhoneResponse::class.java, arrayOfNulls<Annotation>(0))
        val error: VerifyPhoneResponse
        error = try {
            converter.convert(response.errorBody())!!
        } catch (e: IOException) {
            return VerifyPhoneResponse(null, null)
        }
        return error
    }
}