package com.kwdevs.hospitalsdashboard.models.settings.area

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.squareup.moshi.Json

data class AreaWithHospitalDetails(
    @Json(name = "id")
    var id:Int,

    @Json(name = "city_id")
    var cityId:Int,

    @Json(name = "name")
    var name:String,

    @Json(name = "city")
    var city:City,

    @Json(name = "hospitals")
    var hospitals:List<Hospital>?= emptyList()

)
