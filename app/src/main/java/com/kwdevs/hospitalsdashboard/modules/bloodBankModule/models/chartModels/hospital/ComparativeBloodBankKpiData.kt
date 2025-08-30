package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityBloodBankGeneralKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityComparativeBloodBankKpi
import com.squareup.moshi.Json

data class ComparativeBloodBankKpiData(
    @Json(name = "sectors" )  val sectors :List<CityComparativeBloodBankKpi>   = emptyList(),
    @Json(name = "curative" )  val curative :List<CityBloodBankGeneralKpi>   = emptyList(),
    @Json(name = "insurance" )  val insurance :List<CityBloodBankGeneralKpi>   = emptyList(),
    @Json(name = "educational" )  val educational :List<CityBloodBankGeneralKpi>   = emptyList(),
    @Json(name = "specialized" )  val specialized :List<CityBloodBankGeneralKpi>   = emptyList(),
    @Json(name = "directorate" )  val directorate :List<CityBloodBankGeneralKpi>   = emptyList(),
    @Json(name = "nbts" )  val nbts :List<CityBloodBankGeneralKpi>   = emptyList(),

    @Json(name = "reasons")  val reasons:List<IncinerationReason> = emptyList(),
)
