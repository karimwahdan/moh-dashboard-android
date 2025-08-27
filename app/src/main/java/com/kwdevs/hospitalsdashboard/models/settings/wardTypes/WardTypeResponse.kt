package com.kwdevs.hospitalsdashboard.models.settings.wardTypes

import com.squareup.moshi.Json

data class WardTypeResponse(
    @Json(name = "data")
    var data:List<WardType>
)
