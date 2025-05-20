package com.example.mam.entity

import java.text.DecimalFormat
import java.time.Instant

data class Promotion(
    val code: String = "",
    val value: Int = 0,
    val name: String = "",
    val description: String = "",
    val startDate: Instant = Instant.now(),
    val endDate: Instant = Instant.now(),
    val minValue: Int = 0,
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
){
    fun getValueToString(): String {
        val formatter = DecimalFormat("#,###")
        return "- ${formatter.format(value)} VND"
    }
}
