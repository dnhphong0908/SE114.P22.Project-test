package com.example.mam.services

class BaseService(accessToken: String) {
    private val privateRetrofit = RetrofitClient.createPrivateRetrofit(accessToken)
    private val publicRetrofit = RetrofitClient.createPublicRetrofit()
    val authPublicService: AuthPublicService by lazy {
        publicRetrofit.create(AuthPublicService::class.java) }
    val authPrivateService: AuthPrivateService by lazy {
        privateRetrofit.create(AuthPrivateService::class.java) }
}