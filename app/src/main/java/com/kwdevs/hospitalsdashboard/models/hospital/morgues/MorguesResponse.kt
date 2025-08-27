package com.kwdevs.hospitalsdashboard.models.hospital.morgues

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class MorguesResponse(
    @Json(name = "data")
    var data:List<Morgue> = emptyList()
)
