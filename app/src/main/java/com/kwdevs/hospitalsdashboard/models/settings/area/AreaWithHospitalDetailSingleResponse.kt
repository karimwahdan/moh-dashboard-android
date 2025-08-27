package com.kwdevs.hospitalsdashboard.models.settings.area

import com.squareup.moshi.Json

data class AreaWithHospitalDetailSingleResponse(
    @Json(name = "data")
    var data:AreaWithHospitalDetails
)
