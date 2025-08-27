package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment

import com.squareup.moshi.Json

data class BloodComponentOptionsData(
    @Json(name = "data")
    val data:BloodComponentOptionsResponse
)
