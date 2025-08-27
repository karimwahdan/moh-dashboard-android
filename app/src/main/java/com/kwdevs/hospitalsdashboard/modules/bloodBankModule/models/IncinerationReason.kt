package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.squareup.moshi.Json

data class IncinerationReason(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "maximum_percent")
    val maximumPercent: Float,
)
