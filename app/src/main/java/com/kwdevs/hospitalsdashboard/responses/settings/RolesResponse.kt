package com.kwdevs.hospitalsdashboard.responses.settings

import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.squareup.moshi.Json

data class RolesResponse(
    @Json(name = "data")
    val data:List<Role>
)
