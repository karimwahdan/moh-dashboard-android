package com.kwdevs.hospitalsdashboard.models.patients

import com.squareup.moshi.Json

data class PatientsResponse(
    @Json(name = "data")
    val data:List<Patient>
)
