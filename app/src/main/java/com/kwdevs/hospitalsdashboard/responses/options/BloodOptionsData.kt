package com.kwdevs.hospitalsdashboard.responses.options

import com.squareup.moshi.Json

data class BloodOptionsData(
    @Json(name = "data")
    val data:BloodOptionsResponse
)
