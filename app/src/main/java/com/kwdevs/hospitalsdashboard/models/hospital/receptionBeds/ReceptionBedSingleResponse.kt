package com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds

import com.kwdevs.hospitalsdashboard.models.hospital.reception.ReceptionData
import com.squareup.moshi.Json

data class ReceptionBedSingleResponse(
    @Json(name = "data")
    var data:ReceptionData?
)
