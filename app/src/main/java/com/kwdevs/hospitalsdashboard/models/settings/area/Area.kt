package com.kwdevs.hospitalsdashboard.models.settings.area

import com.squareup.moshi.Json

data class Area(
    @Json(name = "id")
    var id:Int?=null,

    @Json(name = "city_id")
    var cityId:Int?=null,

    @Json(name = "name")
    var name:String?=null,
    @Json(name = "head_id")
    val headId:Int?=null,

)
