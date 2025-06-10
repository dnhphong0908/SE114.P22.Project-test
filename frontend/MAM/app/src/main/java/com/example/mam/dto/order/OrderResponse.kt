package com.example.mam.dto.order

import com.example.mam.dto.BaseResponse
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.Instant

data class OrderResponse(
    val shippingAddress: String,
    val totalPrice: BigDecimal,
    val note: String,
    val expectedDeliveryTime: String,
    val actualDeliveryTime: String,
    val orderStatus: String,
    val userId: Long,
    val shipperId: Long,
    val orderDetails: List<OrderDetailResponse>,
): BaseResponse(){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(totalPrice)} VND"
    }
}
