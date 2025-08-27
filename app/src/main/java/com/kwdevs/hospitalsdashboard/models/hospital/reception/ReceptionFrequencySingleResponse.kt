package com.kwdevs.hospitalsdashboard.models.hospital.reception

import com.squareup.moshi.Json

data class ReceptionFrequencySingleResponse(
    @Json(name = "data")
    var data:ReceptionFrequency?
)
