package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.squareup.moshi.Json

data class MonthlyIncinerationSingleResponse(
    @Json(name = "data")
    val data: MonthlyIncineration?=null,
)
