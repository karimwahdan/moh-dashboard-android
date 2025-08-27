package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.squareup.moshi.Json

data class BloodNearExpiredItemSingleResponse(
    @Json(name = "data")
    val data: BloodNearExpiredItem?=null,
)
