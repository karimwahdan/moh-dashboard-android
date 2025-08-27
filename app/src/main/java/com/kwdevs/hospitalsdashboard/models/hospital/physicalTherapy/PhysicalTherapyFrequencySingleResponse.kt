package com.kwdevs.hospitalsdashboard.models.hospital.physicalTherapy

import com.squareup.moshi.Json

data class PhysicalTherapyFrequencySingleResponse(
    @Json(name = "data")
    var data:PhysicalTherapyFrequency?
)
