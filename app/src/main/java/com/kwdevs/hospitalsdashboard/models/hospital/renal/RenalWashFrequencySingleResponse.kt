package com.kwdevs.hospitalsdashboard.models.hospital.renal

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.squareup.moshi.Json

data class RenalWashFrequencySingleResponse(
    @Json(name = "data")
    var data:RenalWashFrequency? = null
)
