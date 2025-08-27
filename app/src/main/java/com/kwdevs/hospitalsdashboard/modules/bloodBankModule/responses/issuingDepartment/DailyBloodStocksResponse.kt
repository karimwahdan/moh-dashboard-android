package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.squareup.moshi.Json

data class DailyBloodStocksResponse(
    @Json(name = "data")
    val data:List<DailyBloodStock> = emptyList(),
)
