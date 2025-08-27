package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital

import com.squareup.moshi.Json

data class HospitalBloodBankKpiResponse(
    @Json(name = "data" )  val data : HospitalBloodBankKpiData?   = null,
)
