package com.kwdevs.hospitalsdashboard.models.patients.cancerCures

import com.squareup.moshi.Json

data class CancerCureResponse(
    @Json(name = "data")
    val data:List<CancerCure>
)
