package com.kwdevs.hospitalsdashboard.models.settings.city

import com.squareup.moshi.Json

data class CityWithCount(
    @Json(name = "id")
    var id:Int?=null,

    @Json(name = "name")
    var name:String?=null,
    @Json(name = "slug")
    var slug:String?=null,

    @Json(name = "areas_count")
    var areasCount:Int?=0,

    @Json(name = "hospitals_count")
    var hospitalsCount:Int?=0,
    @Json(name = "head_id")
    val headId:Int?=null,

)
