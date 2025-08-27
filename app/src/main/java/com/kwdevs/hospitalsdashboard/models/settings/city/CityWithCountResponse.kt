package com.kwdevs.hospitalsdashboard.models.settings.city

import com.squareup.moshi.Json

data class CityWithCountResponse(
    @Json(name = "data")
    var data:List<CityWithCount>
)
