package com.kwdevs.hospitalsdashboard.responses.options

import com.squareup.moshi.Json

data class HospitalWardAdmissionOptionsData(
    @Json(name = "data")
    val data:HospitalWardAdmissionOptionsResponse
)
