package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.squareup.moshi.Json

data class HospitalBloodBankKpiData(
    @Json(name = "hospitals" )  val hospitals :List<HospitalBloodBankKpi>   = emptyList(),
    @Json(name = "reasons")  val reasons:List<IncinerationReason> = emptyList(),
)
