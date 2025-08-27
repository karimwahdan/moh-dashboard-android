package com.kwdevs.hospitalsdashboard.models.settings.area

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.squareup.moshi.Json

data class AreaWithHospitals(
    @Json(name = "id")
    var id:Int,

    @Json(name = "city_id")
    var cityId:Int,

    @Json(name = "name")
    var name:String,

    @Json(name = "hospitals")
    var hospitals:List<SimpleHospital>,
    @Json(name = "head_id")
    val headId:Int?=null,

)
