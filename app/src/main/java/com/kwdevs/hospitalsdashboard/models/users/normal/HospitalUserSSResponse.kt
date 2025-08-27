package com.kwdevs.hospitalsdashboard.models.users.normal

import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.squareup.moshi.Json

data class HospitalUserSSResponse(
    @Json(name = "data")
    val data:SimpleHospitalUser,
)
