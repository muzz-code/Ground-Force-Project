package com.trapezoidlimited.groundforce.repository

import com.trapezoidlimited.groundforce.api.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * Base BaseRepository is inherited by other repositories to make API calls
 * It's going to return a Resource type
 */
abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        /**
         * Safe Api call function takes in the ApiCall function,
         * invoke it and wrap in a Resource response which could be Success of Failure
         */
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            false,
                            throwable.code().toString(),
                            throwable.response() as Response<Any>
                        )
                    }
                    else -> {
                        Resource.Failure(
                            true,
                            null,
                            null
                        )
                    }
                }
            }
        }
    }
}