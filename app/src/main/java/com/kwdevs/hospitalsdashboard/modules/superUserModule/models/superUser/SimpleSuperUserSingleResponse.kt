package com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser

import com.squareup.moshi.Json

data class SimpleSuperUserSingleResponse(
    @Json(name = "data")
    val data: SimpleSuperUser,
)
