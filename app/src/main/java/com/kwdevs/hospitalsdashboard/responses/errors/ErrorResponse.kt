package com.kwdevs.hospitalsdashboard.responses.errors

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "code")
    val code:Int? =404,
    @Json(name="message")
    val message:String?="",
    @Json(name="errors")
    val errors: Map<String, List<String>>? = emptyMap(),

)
