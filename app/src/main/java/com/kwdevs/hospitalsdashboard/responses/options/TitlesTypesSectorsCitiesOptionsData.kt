package com.kwdevs.hospitalsdashboard.responses.options

import com.squareup.moshi.Json

data class TitlesTypesSectorsCitiesOptionsData(
    @Json(name = "data")
    val data:TitlesTypesSectorsCitiesResponse
)
