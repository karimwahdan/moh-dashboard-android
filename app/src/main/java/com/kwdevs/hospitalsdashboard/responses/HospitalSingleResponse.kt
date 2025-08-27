package com.kwdevs.hospitalsdashboard.responses

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class HospitalSingleResponse(
    @Json(name = "data")
    var data:Hospital?
)
