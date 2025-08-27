package com.kwdevs.hospitalsdashboard.models.settings.basicDepartments

import com.squareup.moshi.Json

data class BasicDepartment(
    @Json(name = "id")
    var id:Int,

    @Json(name = "name")
    var name:String,

)
