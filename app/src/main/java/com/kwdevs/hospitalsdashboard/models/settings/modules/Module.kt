package com.kwdevs.hospitalsdashboard.models.settings.modules

import com.squareup.moshi.Json

data class Module(
    @Json(name="id")
    val id:Int?=null,
    @Json(name = "name")
    val name:String?=null,
    @Json(name = "slug")
    val slug:String?=null,
)
