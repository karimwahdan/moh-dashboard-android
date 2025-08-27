package com.kwdevs.hospitalsdashboard.models.settings.nationality

import com.squareup.moshi.Json

data class NationalitiesResponse(
    @Json(name = "data")
    var data:List<Nationality>
)
