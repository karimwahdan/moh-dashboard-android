package com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes

import com.squareup.moshi.Json

data class ClinicVisitTypeResponse(
    @Json(name = "data")
    var data:List<ClinicVisitType>
)
