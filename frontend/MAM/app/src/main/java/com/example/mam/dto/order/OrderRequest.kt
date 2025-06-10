package com.example.mam.dto.order

data class OrderRequest(
    val shippingAddress: String = "",
    val note: String ?= null,
    val paymentMethod: String = "",
    val promotionCodeId: Long ?= null,
)