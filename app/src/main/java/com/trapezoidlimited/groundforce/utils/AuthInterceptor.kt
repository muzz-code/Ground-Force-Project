package com.trapezoidlimited.groundforce.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor(val context: Context): Interceptor {

    val token = SessionManager.load(context, TOKEN)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        SessionManager.load(context, TOKEN )?.let {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}