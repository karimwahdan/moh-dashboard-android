package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.squareup.moshi.Json

data class BloodImportSingleResponse(
    @Json(name = "data")
    val data: BloodImport?=null,
)
