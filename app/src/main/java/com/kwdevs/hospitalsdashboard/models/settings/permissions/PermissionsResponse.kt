package com.kwdevs.hospitalsdashboard.models.settings.permissions

import com.squareup.moshi.Json

data class PermissionsResponse(
    @Json(name = "data")
    var data:List<Permission>
)
