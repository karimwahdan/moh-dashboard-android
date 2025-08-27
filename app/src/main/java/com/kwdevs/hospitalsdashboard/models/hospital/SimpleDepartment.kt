package com.kwdevs.hospitalsdashboard.models.hospital

import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartment
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class SimpleDepartment(
    @Json(name = "id")
    val id: Int?=null,

    @Json(name = "department_bid")
    val basicDepartmentId:Int,
    @Json(name = "hospital_id")
    val hospitalId: Int?=null,

    @Json(name = "name")
    val name: String,

    @Json(name = "active")
    val active: Boolean, // Assuming 'active' is stored as TINYINT(1) in DB

    @Json(name = "basic_department")
    val basicDepartment:BasicDepartment?=null,
)
