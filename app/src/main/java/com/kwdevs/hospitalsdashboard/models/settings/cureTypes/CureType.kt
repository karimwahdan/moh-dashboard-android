package com.kwdevs.hospitalsdashboard.models.settings.cureTypes

import com.squareup.moshi.Json

data class CureType(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,

)
