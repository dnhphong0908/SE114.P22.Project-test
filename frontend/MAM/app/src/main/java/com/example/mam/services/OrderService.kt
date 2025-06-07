package com.example.mam.services

import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    @PUT("orders/update/{id}")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>
    @POST("orders/create")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<OrderResponse>
    @PUT("orders/cancel/{id}")
    suspend fun cancelOrder(@Path("id") id: String): Response<OrderResponse>
    @GET("orders")
    suspend fun getAllOrders(
        @Query("pageable") pageable: String,
        @Query("specification") specification: String
    ): Response<PageVO<OrderResponse>>
    //Chưa xongg
}