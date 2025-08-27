package com.kwdevs.hospitalsdashboard.models.settings.sector

import com.squareup.moshi.Json

data class SectorResponse(
    @Json(name = "data")
    var data:List<Sector>
)
