package com.kwdevs.hospitalsdashboard.models.hospital.labTests

import com.squareup.moshi.Json

data class LabTestFrequencySingleResponse(
    @Json(name = "data")
    var data:LabTestFrequency?
)
