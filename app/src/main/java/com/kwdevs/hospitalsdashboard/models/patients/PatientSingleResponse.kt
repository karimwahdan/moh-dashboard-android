package com.kwdevs.hospitalsdashboard.models.patients

import com.squareup.moshi.Json

data class PatientSingleResponse(
    @Json(name = "data")
    val data:Patient
)
