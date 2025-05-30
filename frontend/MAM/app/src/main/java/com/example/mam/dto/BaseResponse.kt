package com.example.mam.dto

import java.time.Instant

open class BaseResponse(
    val id: Long = 0,
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
    val createBy: Long = 0,
    val updateBy: Long = 0,
)
