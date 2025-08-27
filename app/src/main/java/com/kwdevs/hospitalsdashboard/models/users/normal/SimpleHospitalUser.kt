package com.kwdevs.hospitalsdashboard.models.users.normal

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.logs.HospitalUserLog
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.squareup.moshi.Json

data class SimpleHospitalUser(
    @Json(name = "id")
    val id: Int,

    @Json(name = "hospital_id")
    val hospitalId: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "title_id")
    val titleId: Int? = null,

    @Json(name = "active")
    val active: Boolean, // Laravel: cast tinyint(1) to boolean in model

    // Optional nested relationships (included if API returns them)
    @Json(name = "title")
    val title: Title?=null,

    @Json(name = "hospital")
    val hospital: SimpleHospital,

    @Json(name = "head_of")
    val headOf: HospitalDepartment? = null,

    @Json(name = "deputy_of")
    val deputyOf: HospitalDepartment? = null,

    @Json(name = "roles")
    val roles:List<Role> = emptyList()

)
