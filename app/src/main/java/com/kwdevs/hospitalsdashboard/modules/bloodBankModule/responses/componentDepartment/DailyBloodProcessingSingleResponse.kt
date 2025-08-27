package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class DailyBloodProcessingSingleResponse(
    @Json(name = "data")
    val data: DailyBloodProcessing?=null,
)
