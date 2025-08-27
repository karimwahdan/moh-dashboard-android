package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.squareup.moshi.Json

data class CityBloodBankKpiData(
    @Json(name = "cities" )  val cities :List<CityBloodBankKpi>   = emptyList(),
    @Json(name = "reasons")  val reasons:List<IncinerationReason> = emptyList(),
)
