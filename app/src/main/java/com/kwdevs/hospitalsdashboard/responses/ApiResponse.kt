package com.kwdevs.hospitalsdashboard.responses

import com.squareup.moshi.Json

data class ApiResponse<T>(
    @Json(name = "code")
    val code:Int,
    @Json(name = "message")
    val message:String,

    @Json(name = "data")
    val pagination: T,
)
