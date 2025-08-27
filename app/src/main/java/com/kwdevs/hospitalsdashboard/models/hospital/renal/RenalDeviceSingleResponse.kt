package com.kwdevs.hospitalsdashboard.models.hospital.renal

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.squareup.moshi.Json

data class RenalDeviceSingleResponse(
    @Json(name = "data")
    var data:HospitalRenalDevice? = null
)
