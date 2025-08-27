package com.kwdevs.hospitalsdashboard.responses.settings

import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.squareup.moshi.Json

data class CustomModelSingleResponse(
    @Json(name = "data")
    val data:CustomModel,
)
