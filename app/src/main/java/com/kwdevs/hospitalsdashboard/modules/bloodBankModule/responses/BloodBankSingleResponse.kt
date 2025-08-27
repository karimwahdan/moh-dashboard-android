package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.squareup.moshi.Json

data class BloodBankSingleResponse(
    @Json(name = "data")
    val data:BloodBank?=null,
)
