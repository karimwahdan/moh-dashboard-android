package com.kwdevs.hospitalsdashboard.models.hospital

import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartment
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class HospitalDepartment(
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

    @Json(name = "head_id")
    val headId: Int? = null,

    @Json(name = "deputy_id")
    val deputyId: Int? = null,

    @Json(name = "updated_by_id")
    val updatedById: Int? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    @Json(name = "updated_by_super_id")
    val updatedBySuperId: Int? = null,

    @Json(name = "updated_at_super")
    val updatedAtSuper: String? = null,

    @Json(name = "deleted_by_id")
    val deletedById: Int? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "deleted_by_super_id")
    val deletedBySuperId: Int? = null,

    @Json(name = "deleted_at_user")
    val deletedAtUser: String? = null,

    // --- Related Objects (Optional) ---
    @Json(name = "hospital")
    val hospital: SimpleHospital? = null,

    @Json(name = "basic_department")
    val basicDepartment:BasicDepartment?=null,

    @Json(name = "head")
    val head: SimpleHospitalUser? = null,

    @Json(name = "deputy")
    val deputy: SimpleHospitalUser? = null,

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null,

    @Json(name = "members")
    val members: List<DepartmentMember> = emptyList()
)
