package com.example.mam.services

import com.example.mam.dto.product.CategoryRequest
import com.example.mam.dto.product.ProductRequest
import com.example.mam.dto.product.ProductResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @POST("products")
    suspend fun createProduct(@Body request: ProductRequest): Response<ProductResponse>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body request: ProductRequest): Response<ProductResponse>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Void>

    @GET("products/category/{id}")
    suspend fun getProductsByCategory(
        @Path("id") id: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>? = null,
        @Query("specification") specification: String): Response<PageVO<ProductResponse>>
}