package com.example.mam.services

//BaseService để khởi tạo Retrofit client và gọi các service
class BaseService(accessToken: String) {
    private val privateRetrofit = RetrofitClient.createPrivateRetrofit(accessToken)

    private val publicRetrofit = RetrofitClient.createPublicRetrofit()
    // Các service sẽ được khởi tạo ở đây, sử dụng lazy để chỉ khởi tạo khi cần thiết
    val authPublicService: AuthPublicService by lazy {
        publicRetrofit.create(AuthPublicService::class.java) }

    val authPrivateService: AuthPrivateService by lazy {
        privateRetrofit.create(AuthPrivateService::class.java) }
}