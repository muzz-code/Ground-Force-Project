package com.trapezoidlimited.groundforce.api

import retrofit2.Response

/**
 * Sealed class to help manage the network call responses */

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: Response<Any>?
    ) : Resource<Nothing>()

}

