package com.kwdevs.hospitalsdashboard.models.settings.generalClinics

import com.squareup.moshi.Json

data class GeneralClinic(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "name")
    val name:String?=null,

)
