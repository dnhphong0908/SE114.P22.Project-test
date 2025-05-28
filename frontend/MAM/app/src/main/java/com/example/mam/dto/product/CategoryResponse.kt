package com.example.mam.dto.product

import com.example.mam.dto.BaseResponse

data class CategoryResponse(
    val name: String,
    val description: String,
    val imageUrl: String,
): BaseResponse()
