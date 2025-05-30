package com.example.mam.dto.order

enum class PaymentMethod {
    CASH_ON_DELIVERY,
    VNPAY,
    MOMO,
    ZALOPAY,
    BANK_TRANSFER,
}
data class OrderRequest(
    val shippingAddress: String,
    val note: String,
    val paymentMethod: PaymentMethod
)