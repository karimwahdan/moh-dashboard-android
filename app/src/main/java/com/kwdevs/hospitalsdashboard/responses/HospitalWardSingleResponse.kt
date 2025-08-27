package com.kwdevs.hospitalsdashboard.responses

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.squareup.moshi.Json

data class HospitalWardSingleResponse(
    @Json(name = "data")
    var data:HospitalWard?
)
