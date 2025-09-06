package com.kwdevs.hospitalsdashboard.models.users.normal

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.logs.HospitalUserLog
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.squareup.moshi.Json

data class HospitalUser(
    @Json(name = "id") val id: Int,

    @Json(name = "hospital_id") val hospitalId: Int,

    @Json(name = "name") val name: String,

    @Json(name = "username") val username: String,

    @Json(name = "password") val password: String, // optional: hash usually hidden in API

    @Json(name = "title_id") val titleId: Int? = null,

    @Json(name = "national_id") val nationalId: String? = null,

    @Json(name = "active") val active: Boolean,

    @Json(name = "title") val title: Title?=null,

    @Json(name = "hospital") val hospital: SimpleHospital,

    @Json(name = "head_of") val headOf: HospitalDepartment? = null,

    @Json(name = "deputy_of") val deputyOf: HospitalDepartment? = null,

    @Json(name = "logs") val logs: List<HospitalUserLog> = emptyList(),

    @Json(name = "roles") val roles:List<Role> = emptyList()
)
