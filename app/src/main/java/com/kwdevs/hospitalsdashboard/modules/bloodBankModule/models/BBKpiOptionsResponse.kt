package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.squareup.moshi.Json

data class BBKpiOptionsResponse(
    @Json(name = "data") val data:BBKpiOptionsData
)
