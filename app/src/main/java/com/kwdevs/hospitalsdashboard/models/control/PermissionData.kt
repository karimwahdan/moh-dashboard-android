package com.kwdevs.hospitalsdashboard.models.control

import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class PermissionData(
    @Json(name = "normal_users")
    val users:List<SimpleHospitalUser>,

    @Json(name = "super_users")
    val superUsers:List<SimpleSuperUser>,

    @Json(name = "models")
    val models:List<CustomModel>,

    @Json(name = "roles")
    val roles:List<Role>,

    @Json(name = "action_types")
    val actionTypes:List<ActionType>,
)
