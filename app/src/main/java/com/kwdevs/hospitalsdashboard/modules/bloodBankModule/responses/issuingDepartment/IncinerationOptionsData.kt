package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.squareup.moshi.Json

data class IncinerationOptionsData(
    @Json(name = "data")
    val data:IncinerationOptionsResponse
)
