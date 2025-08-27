package com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes

import com.squareup.moshi.Json

data class ClinicVisitType(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "name")
    val name:String?=null,
)
