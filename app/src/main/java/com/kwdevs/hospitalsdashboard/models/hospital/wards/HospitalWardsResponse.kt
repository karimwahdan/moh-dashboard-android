package com.kwdevs.hospitalsdashboard.models.hospital.wards

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.squareup.moshi.Json

data class HospitalWardsResponse(
    @Json(name = "data")
    var data:List<HospitalWard> = emptyList()
)
