package com.example.mam.dto.shipper

import java.time.Instant

data class ShipperResponse(
    val id: Long,
    val createdAt: Instant,
    val updatedAt: Instant,

    val fullname: String,
    val phone: String,
    val licensePlate: String
)
