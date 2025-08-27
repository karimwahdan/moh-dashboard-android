package com.kwdevs.hospitalsdashboard.models.settings.area

import com.squareup.moshi.Json

data class AreaResponse(
    @Json(name = "data")
    var data:List<Area>
)
