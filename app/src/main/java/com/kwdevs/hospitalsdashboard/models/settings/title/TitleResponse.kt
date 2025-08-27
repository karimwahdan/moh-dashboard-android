package com.kwdevs.hospitalsdashboard.models.settings.title

import com.squareup.moshi.Json

data class TitleResponse(
    @Json(name = "data")
    var data:List<Title>
)
