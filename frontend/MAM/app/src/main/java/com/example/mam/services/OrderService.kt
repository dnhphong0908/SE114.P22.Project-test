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
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 24,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<OrderResponse>>
    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<OrderResponse>
    @GET("orders/me")
    suspend fun getCurrentUserOrders(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<OrderResponse>>
    @GET("orders/delete/{id}")
    suspend fun deleteOrder(@Path("id") id: String): Response<OrderResponse>
}