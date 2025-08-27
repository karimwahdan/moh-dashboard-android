package com.kwdevs.hospitalsdashboard.responses.settings

import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.squareup.moshi.Json

data class HospitalCategoriesResponse(
    @Json(name = "data")
    val data:List<Sector>,
)
