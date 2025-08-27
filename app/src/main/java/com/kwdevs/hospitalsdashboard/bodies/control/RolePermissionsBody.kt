package com.kwdevs.hospitalsdashboard.bodies.control

import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission

data class RolePermissionsBody(
    val id:Int?=null,
    val permissions:List<Int> = emptyList()
)
