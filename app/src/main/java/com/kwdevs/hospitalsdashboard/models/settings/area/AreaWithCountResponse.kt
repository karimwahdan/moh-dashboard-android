package com.kwdevs.hospitalsdashboard.models.settings.area

import com.squareup.moshi.Json

data class AreaWithCountResponse(
    @Json(name = "data")
    var data:List<AreaWithCount>
)
