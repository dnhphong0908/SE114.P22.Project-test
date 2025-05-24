package com.example.mam.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://159.223.62.237/api/v1/"
    private val authInterceptor = Interceptor { chain ->
        val accessToken = "" // Replace with logic to retrieve the access token from DataStoreManager or other source

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        chain.proceed(request)
    }

    val okHttpClientWithAuth = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val authPublic: AuthPublicService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthPublicService::class.java)
    }

    val user: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClientWithAuth)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}