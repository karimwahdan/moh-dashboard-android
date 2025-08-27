package com.kwdevs.hospitalsdashboard.models.settings.city

import com.squareup.moshi.Json

data class CityResponse(
    @Json(name = "data")
    var data:List<City>
)
