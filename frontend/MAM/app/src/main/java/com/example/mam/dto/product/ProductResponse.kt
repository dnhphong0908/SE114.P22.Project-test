package com.example.mam.dto.product
import com.example.mam.dto.BaseResponse
import java.math.BigDecimal
import java.text.DecimalFormat

class ProductResponse (
    val name: String,
    val shortDescription: String,
    val detailDescription: String,
    val originalPrice: BigDecimal,
    val imageUrl: String,
    val isAvailable: Boolean,
    val categoryId: Long,
    val categoryName: String,
) : BaseResponse(){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(originalPrice)} VND"
    }
}
