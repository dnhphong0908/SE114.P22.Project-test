package com.example.mam.services

import com.example.mam.dto.cart.CartItemRequest
import com.example.mam.dto.cart.CartItemResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartService {
    @GET("carts/{id}")
    suspend fun getCart(@Path("id") id: Long): CartItemResponse

}