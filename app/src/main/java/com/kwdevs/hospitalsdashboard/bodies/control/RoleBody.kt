package com.kwdevs.hospitalsdashboard.bodies.control

import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission

data class RoleBody(
    val id:Int?=null,
    val name:String,
    val slug:String,
    val permissions:List<Permission>? = emptyList()
)
