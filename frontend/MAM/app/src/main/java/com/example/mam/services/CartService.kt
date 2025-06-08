package com.example.mam.services

import com.example.mam.dto.cart.CartResponse
import retrofit2.Response
import retrofit2.http.GET

interface CartService {
    @GET("carts/me")
    suspend fun getMyCart(): Response<CartResponse>

}