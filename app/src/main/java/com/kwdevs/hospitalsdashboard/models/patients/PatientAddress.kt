package com.kwdevs.hospitalsdashboard.models.patients

import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.squareup.moshi.Json

data class PatientAddress(
    @Json(name = "id")
    val id:Int?=null,
    val patientId:Int?=null,
    val cityId:Int?=null,
    val areaId:Int?=null,
    val city: City?=null,
    val area: Area?=null,
)
