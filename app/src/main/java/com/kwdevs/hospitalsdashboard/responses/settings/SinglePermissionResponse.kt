package com.kwdevs.hospitalsdashboard.responses.settings

import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.squareup.moshi.Json

data class SinglePermissionResponse(
    @Json(name = "data")
    val data:Permission,
)
