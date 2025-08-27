package com.kwdevs.hospitalsdashboard.models.settings

import com.squareup.moshi.Json

data class CustomModel(
    @Json(name = "id")
    val id:Int,

    @Json(name = "model_name")
    val name:String,

    @Json(name = "column_list")
    val columns:List<String>
)
