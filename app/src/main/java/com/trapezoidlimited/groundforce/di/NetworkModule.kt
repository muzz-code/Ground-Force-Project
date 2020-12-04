package com.trapezoidlimited.groundforce.di

import android.content.Context
import android.util.Log
import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.OtpAuthService
import com.trapezoidlimited.groundforce.data.BASE_URL
import com.trapezoidlimited.groundforce.utils.AuthInterceptor
import com.trapezoidlimited.groundforce.utils.SessionManager
import com.trapezoidlimited.groundforce.utils.TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Module class for Network
 */

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {
    /**
     * Creates the api service
     */


    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    }


    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }


//    @Provides
//    @Singleton
//    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
//        return AuthInterceptor(context)
//    }


    @Provides
    @Singleton
    fun provideClient(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideService(client: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideOtpApiService(retrofit: Retrofit): OtpAuthService {
        return retrofit.create(OtpAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginAuthApi {
        return retrofit.create(LoginAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMissionApiService(retrofit: Retrofit): MissionsApi {
        return retrofit.create(MissionsApi::class.java)
    }


}