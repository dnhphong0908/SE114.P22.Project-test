package com.example.mam.services

import androidx.navigation.NavController
import com.example.mam.data.UserPreferencesRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.45.234.225:8080/api/v1/"

    //tạo Retrofit client cho public
    fun createPublicRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //tạo Retrofit client cho private service, cần truyền access token vào
    fun createPrivateRetrofit(
        userPreferencesRepository: UserPreferencesRepository
    ): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(userPreferencesRepository))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}