package com.kwdevs.hospitalsdashboard.responses.options

import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardType
import com.squareup.moshi.Json

data class HospitalWardAdmissionOptionsResponse(
    @Json(name = "states")
    val states:List<PatientState> = emptyList(),
    @Json(name = "types")
    val types:List<WardType> = emptyList(),
)
