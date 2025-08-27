package com.kwdevs.hospitalsdashboard.models.settings.operations

import com.squareup.moshi.Json

data class OperationStatus(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,
    @Json(name = "color")
    val color:String,

)
