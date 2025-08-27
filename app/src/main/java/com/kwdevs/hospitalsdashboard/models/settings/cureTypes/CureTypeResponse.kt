package com.kwdevs.hospitalsdashboard.models.settings.cureTypes

import com.squareup.moshi.Json

data class CureTypeResponse(
    @Json(name = "data")
    var data:List<CureType>
)
