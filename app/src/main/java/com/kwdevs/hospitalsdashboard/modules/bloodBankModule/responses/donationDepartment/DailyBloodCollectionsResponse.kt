package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class DailyBloodCollectionsResponse(
    @Json(name = "data")
    val data:List<DailyBloodCollection> = emptyList(),
)
