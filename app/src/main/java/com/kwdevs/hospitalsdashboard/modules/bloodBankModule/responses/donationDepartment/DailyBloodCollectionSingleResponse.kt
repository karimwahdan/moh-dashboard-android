package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class DailyBloodCollectionSingleResponse(
    @Json(name = "data")
    val data: DailyBloodCollection?=null,
)
