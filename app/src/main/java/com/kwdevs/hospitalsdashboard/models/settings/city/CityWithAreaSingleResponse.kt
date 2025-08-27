package com.kwdevs.hospitalsdashboard.models.settings.city

import com.squareup.moshi.Json

data class CityWithAreaSingleResponse(
    @Json(name = "data")
    var data:CityWithAreas
)
