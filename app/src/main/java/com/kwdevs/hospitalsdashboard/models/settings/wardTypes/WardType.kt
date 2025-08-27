package com.kwdevs.hospitalsdashboard.models.settings.wardTypes

import com.squareup.moshi.Json

data class WardType(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,

)
