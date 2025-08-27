package com.kwdevs.hospitalsdashboard.models.settings.hospitalType

import com.squareup.moshi.Json

data class HospitalTypeResponse(
    @Json(name = "data")
    var data:List<HospitalType>
)
