package com.kwdevs.hospitalsdashboard.models.settings.statuses

import com.squareup.moshi.Json

data class Status(
    @Json(name = "id")
    var id:Int,

    @Json(name = "name")
    var name:String,

    @Json(name = "color")
    var color:String,


)
