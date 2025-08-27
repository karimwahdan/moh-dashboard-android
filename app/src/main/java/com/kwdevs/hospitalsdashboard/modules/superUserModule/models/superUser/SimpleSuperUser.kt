package com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser

import com.kwdevs.hospitalsdashboard.models.logs.SuperUserLog
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.squareup.moshi.Json

data class SimpleSuperUser(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String,

    @Json(name = "title_id")
    val titleId: Int,

    @Json(name = "active")
    val active: Boolean,

    @Json(name = "is_super")
    val isSuper: Boolean,

    // --- Related Models (Optional) ---
    @Json(name = "title")
    val title: Title? = null,


    @Json(name = "roles")
    val roles:List<Role> = emptyList()

)
