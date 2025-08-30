package com.kwdevs.hospitalsdashboard.models.settings.city

import com.squareup.moshi.Json

data class City(
    @Json(name = "id")
    var id:Int?=null,

    @Json(name = "name")
    var name:String?=null,
    @Json(name = "slug")
    var slug:String?=null,
    @Json(name = "head_id")
    val headId:Int?=null,


)
