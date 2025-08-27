package com.kwdevs.hospitalsdashboard.models.settings.operations

import com.squareup.moshi.Json

data class OperationType(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

)
