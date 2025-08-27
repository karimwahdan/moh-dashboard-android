package com.kwdevs.hospitalsdashboard.models.logs

import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.squareup.moshi.Json

data class SuperUserLog(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "user_id")
    val userId: Int,

    @Json(name = "action_type_id")
    val actionTypeId: Int,

    @Json(name = "model")
    val model: String,

    @Json(name = "created_at")
    val createdAt: String? = null,

    // --- Related Models (Optional) ---
    @Json(name = "user")
    val user: SimpleSuperUser? = null,

    @Json(name = "type")
    val type: ActionType? = null
)

