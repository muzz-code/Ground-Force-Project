package com.trapezoidlimited.groundforce.api

import com.trapezoidlimited.groundforce.data.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Connect to api
 **/

interface ApiService {



 companion object{

     fun create(): Retrofit {
             //log info
             val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

             //create http client
             val client = OkHttpClient.Builder()
                 .addInterceptor(logger)
                 .build()

             //return the retrofit service
             return Retrofit.Builder()
                 .baseUrl(BASE_URL)
                 .client(client)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()

         }

 }
}