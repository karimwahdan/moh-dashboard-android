package com.kwdevs.hospitalsdashboard.responses.options

import com.squareup.moshi.Json

data class MorgueOptionsData(
    @Json(name = "data")
    val data:MorgueOptionsResponse
)
