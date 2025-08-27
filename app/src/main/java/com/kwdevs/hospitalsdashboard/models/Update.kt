package com.kwdevs.hospitalsdashboard.models

import com.squareup.moshi.Json

data class Update(
    @Json(name = "version_code")
    var vCode:Int,

    @Json(name = "requires_update")
    var requiresUpdate:Int,
)
