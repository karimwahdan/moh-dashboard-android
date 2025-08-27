package com.kwdevs.hospitalsdashboard.models.settings

import com.squareup.moshi.Json

data class BirthType(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,

)
