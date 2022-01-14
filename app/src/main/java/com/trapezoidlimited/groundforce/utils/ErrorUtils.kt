package com.trapezoidlimited.groundforce.utils

import com.trapezoidlimited.groundforce.model.response.GenericResponseClass
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
    fun parseError(response: Response<*>): GenericResponseClass<String> {
        val converter: Converter<ResponseBody, GenericResponseClass<String>> = retrofit
            .responseBodyConverter(GenericResponseClass::class.java, arrayOfNulls<Annotation>(0))
        val error: GenericResponseClass<String>
        error = try {
            converter.convert(response.errorBody()!!)!!
        } catch (e: IOException) {
            return GenericResponseClass("null", null, null)
        }
        return error
    }
}