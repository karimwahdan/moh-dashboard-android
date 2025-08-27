package com.kwdevs.hospitalsdashboard.responses.home

import com.squareup.moshi.Json

data class HomeResponse(
    @Json(name = "data")
    val data:HomeData,
)
