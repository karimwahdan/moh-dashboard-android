package com.kwdevs.hospitalsdashboard.models.settings.nationality

import com.squareup.moshi.Json

data class Nationality(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,

)
