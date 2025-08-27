package com.kwdevs.hospitalsdashboard.responses.options

import com.squareup.moshi.Json

data class OperationOptionsData(
    @Json(name = "data")
    val data:OperationOptionsResponse
)
