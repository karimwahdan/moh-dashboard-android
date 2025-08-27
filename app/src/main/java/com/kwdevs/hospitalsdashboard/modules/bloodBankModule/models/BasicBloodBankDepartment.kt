package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.squareup.moshi.Json

data class BasicBloodBankDepartment(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "name")
    val name:String?=null,
)
