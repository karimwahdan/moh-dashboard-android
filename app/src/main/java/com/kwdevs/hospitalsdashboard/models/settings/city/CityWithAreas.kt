package com.kwdevs.hospitalsdashboard.models.settings.city

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.squareup.moshi.Json

data class CityWithAreas(
    @Json(name = "id")
    var id:Int,

    @Json(name = "name")
    var name:String,

    @Json(name = "areas")
    var areas:List<AreaWithCount>? = emptyList(),


    @Json(name = "hospitals")
    var hospitals:List<Hospital>? = emptyList(),
    @Json(name = "head_id")
    val headId:Int?=null,

)
