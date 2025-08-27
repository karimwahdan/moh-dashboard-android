package com.kwdevs.hospitalsdashboard.models.patients.babyBirth

import com.squareup.moshi.Json

data class BabyBirthResponse(
    @Json(name = "data")
    val data:List<BabyBirth>
)
