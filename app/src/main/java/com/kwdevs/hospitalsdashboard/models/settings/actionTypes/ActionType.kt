package com.kwdevs.hospitalsdashboard.models.settings.actionTypes

import com.squareup.moshi.Json

data class ActionType(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "slug")
    val slug: String,

    @Json(name = "color")
    val color: String,

    @Json(name = "font_color")
    val fontColor:String,
)
