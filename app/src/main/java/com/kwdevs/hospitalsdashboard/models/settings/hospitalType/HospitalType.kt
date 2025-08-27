package com.kwdevs.hospitalsdashboard.models.settings.hospitalType

import com.squareup.moshi.Json

data class HospitalType(
    @Json(name = "id")
    var id:Int,

    @Json(name = "name")
    var name:String,

    @Json(name = "icon")
    var icon:String? = null,

    @Json(name = "hospitals_count")
    var hospitalsCount:Int?,
    @Json(name = "head_id")
    val headId:Int?=null,
)
