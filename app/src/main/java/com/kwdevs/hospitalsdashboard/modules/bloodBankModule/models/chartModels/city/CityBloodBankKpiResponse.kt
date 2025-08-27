package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city

import com.squareup.moshi.Json

data class CityBloodBankKpiResponse(
    @Json(name = "data" )  val data : CityBloodBankKpiData?   = null,
)
