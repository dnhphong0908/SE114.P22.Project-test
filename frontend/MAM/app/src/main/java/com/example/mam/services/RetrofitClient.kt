package com.example.mam.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.123.12:8080/api/v1/"
//tạo Retrofit client cho public
    fun createPublicRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //tạo Retrofit client cho private service, cần truyền access token vào
    fun createPrivateRetrofit(accessToken: String): Retrofit {
        val authInterceptor = Interceptor { chain ->// Replace with logic to retrieve the access token from DataStoreManager or other source
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    val authPublic: AuthPublicService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(AuthPublicService::class.java)
//    }
//
//    val user: UserService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClientWithAuth)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(UserService::class.java)
//    }
}