package com.example.mam.dto.product

import android.os.Build.VERSION_CODES.BASE
import com.example.mam.dto.BaseResponse
import com.example.mam.services.BASE_URL

data class CategoryResponse(
    val name: String,
    val description: String,
    val imageUrl: String,
): BaseResponse(){
    fun getRealURL(): String {
        return BASE_URL + imageUrl
    }
}
