package com.kwdevs.hospitalsdashboard.models.settings.permissions

import com.squareup.moshi.Json

data class PermissionsData(
    @Json(name = "permissions") var permissions:List<Permission> = emptyList(),
    @Json(name = "super_permissions") var superPermissions:List<Permission> = emptyList(),

    )
