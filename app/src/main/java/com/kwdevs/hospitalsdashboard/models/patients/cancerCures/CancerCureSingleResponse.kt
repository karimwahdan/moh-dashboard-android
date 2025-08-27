package com.kwdevs.hospitalsdashboard.models.patients.cancerCures

import com.squareup.moshi.Json

data class CancerCureSingleResponse(
    @Json(name = "data")
    val data:CancerCure
)
