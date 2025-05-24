package com.example.mam.dto

import java.time.Instant

data class BaseResponse(
    val id: Long,
    val createAt: Instant,
    val updateAt: Instant,
    val createBy: Long,
    val updateBy: Long,
)
