package com.kwdevs.hospitalsdashboard.models.settings.title

import com.squareup.moshi.Json

data class Title(
    @Json(name = "id")
    var id:Int,

    @Json(name = "name")
    var name:String,

)
