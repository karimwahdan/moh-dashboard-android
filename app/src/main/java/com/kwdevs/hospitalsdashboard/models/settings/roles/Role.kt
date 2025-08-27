package com.kwdevs.hospitalsdashboard.models.settings.roles

import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.squareup.moshi.Json

data class Role(
    @Json(name = "id")
    val id:Int,

    @Json(name = "name")
    val name:String,

    @Json(name = "slug")
    val slug:String,

    @Json(name = "permissions")
    val permissions:List<Permission>
)
