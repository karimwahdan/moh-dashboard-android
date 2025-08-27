package com.kwdevs.hospitalsdashboard.models.hospital.morgues

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class MorgueSingleResponse(
    @Json(name = "data")
    var data:Morgue?
)
