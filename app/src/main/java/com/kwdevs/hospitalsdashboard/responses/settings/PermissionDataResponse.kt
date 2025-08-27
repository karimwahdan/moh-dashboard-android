package com.kwdevs.hospitalsdashboard.responses.settings

import com.kwdevs.hospitalsdashboard.models.control.PermissionData
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.squareup.moshi.Json

data class PermissionDataResponse(
    @Json(name = "data")
    val data:PermissionData,
)
