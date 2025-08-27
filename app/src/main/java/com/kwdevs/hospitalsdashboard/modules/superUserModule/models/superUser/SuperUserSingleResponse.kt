package com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser

import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.squareup.moshi.Json

data class SuperUserSingleResponse(
    @Json(name = "data")
    val data: SuperUser,
)
