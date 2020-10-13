package com.trapezoidlimited.groundforce.api

import okhttp3.ResponseBody

/**
 * Sealed class to help manage the network call responses */

sealed class Resource<out T> {
    data class Success<out T>(val value: T): Resource<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ): Resource<Nothing>()

}