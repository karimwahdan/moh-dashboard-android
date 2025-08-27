package com.kwdevs.hospitalsdashboard.models.settings.area

import com.squareup.moshi.Json

data class AreaWithCount(
    @Json(name = "id")
    var id:Int,

    @Json(name = "city_id")
    var cityId:Int,

    @Json(name = "name")
    var name:String?=null,

    @Json(name = "hospitals_count")
    var hospitalsCount:Int?=0,
    @Json(name = "head_id")
    val headId:Int?=null,

)
