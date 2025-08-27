package com.kwdevs.hospitalsdashboard.models.patients.babyBirth

import com.squareup.moshi.Json

data class BabyBirthSingleResponse(
    @Json(name = "data")
    val data:BabyBirth
)
