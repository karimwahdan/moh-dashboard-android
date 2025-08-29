package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.squareup.moshi.Json

data class ComparativeBloodBankKpiData(
    @Json(name = "curative" )  val curative :List<HospitalBloodBankKpi>   = emptyList(),
    @Json(name = "insurance" )  val insurance :List<HospitalBloodBankKpi>   = emptyList(),
    @Json(name = "educational" )  val educational :List<HospitalBloodBankKpi>   = emptyList(),
    @Json(name = "specialized" )  val specialized :List<HospitalBloodBankKpi>   = emptyList(),
    @Json(name = "directorate" )  val directorate :List<HospitalBloodBankKpi>   = emptyList(),
    @Json(name = "nbts" )  val nbts :List<HospitalBloodBankKpi>   = emptyList(),

    @Json(name = "reasons")  val reasons:List<IncinerationReason> = emptyList(),
)
