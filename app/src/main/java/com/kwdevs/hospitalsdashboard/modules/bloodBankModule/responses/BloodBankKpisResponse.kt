package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpi
import com.squareup.moshi.Json

data class BloodBankKpisResponse(
    @Json(name = "data")
    val data:List<BloodBankKpi>?= emptyList(),
)
