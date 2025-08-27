package com.kwdevs.hospitalsdashboard.models.patients.preterms

import com.squareup.moshi.Json

data class PretermAdmissionSingleResponse(
    @Json(name = "data")
    val data: PretermAdmission
)
