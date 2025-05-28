package com.example.mam.dto.product

import com.example.mam.dto.BaseResponse
import okhttp3.MultipartBody

data class CategoryRequest(
    val name: String,
    val description: String,
    val image: MultipartBody.Part,
)
