package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.squareup.moshi.Json

data class MonthlyIssuingReportSingleResponse(
    @Json(name = "data")
    val data: MonthlyIssuingReport?=null,
)
