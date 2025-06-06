package com.example.mam.services

import com.example.mam.dto.cart.CartItemRequest
import com.example.mam.dto.cart.CartItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CartItemService {
    @POST("cart-items")
    suspend fun addCartItem(
        @Body cartItemRequest: CartItemRequest
    ): Response<CartItemResponse>
}