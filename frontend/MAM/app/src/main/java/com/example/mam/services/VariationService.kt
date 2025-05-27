package com.example.mam.services

import com.example.mam.dto.variation.VariationRequest
import com.example.mam.dto.variation.VariationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VariationService {
    @PUT("variations/{id}")
    suspend fun updateVariation(@Path("id") id: Long): Response<VariationResponse>
    @DELETE("variations/{id}")
    suspend fun deleteVariation(@Path("id") id: Long): Response<Unit>
    @POST("variations")
    suspend fun createVariation(@Body variationRequest: VariationRequest): Response<VariationResponse>
}