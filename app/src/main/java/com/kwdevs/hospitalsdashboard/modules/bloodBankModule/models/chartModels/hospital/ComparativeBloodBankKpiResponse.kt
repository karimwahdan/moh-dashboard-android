package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital

import com.squareup.moshi.Json

data class ComparativeBloodBankKpiResponse(
    @Json(name = "data" )  val data : ComparativeBloodBankKpiData?   = null,
)
