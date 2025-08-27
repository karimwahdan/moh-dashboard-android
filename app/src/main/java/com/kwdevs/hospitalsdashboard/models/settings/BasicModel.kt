package com.kwdevs.hospitalsdashboard.models.settings

import com.squareup.moshi.Json

data class BasicModel(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "name")
    val name:String?=null,
)
