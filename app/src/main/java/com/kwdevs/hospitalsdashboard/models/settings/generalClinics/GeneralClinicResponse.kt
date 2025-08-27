package com.kwdevs.hospitalsdashboard.models.settings.generalClinics

import com.squareup.moshi.Json

data class GeneralClinicResponse(
    @Json(name = "data")
    var data:List<GeneralClinic>
)
