package com.kwdevs.hospitalsdashboard.models.hospital.incubators

import com.squareup.moshi.Json

data class IncubatorSingleResponse(
    @Json(name = "data")
    var data:Incubator?,
)
