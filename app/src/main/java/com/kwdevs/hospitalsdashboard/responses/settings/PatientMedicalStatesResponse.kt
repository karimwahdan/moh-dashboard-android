package com.kwdevs.hospitalsdashboard.responses.settings

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.squareup.moshi.Json

data class PatientMedicalStatesResponse(
    @Json(name = "data")
    var data:List<PatientState>
)
