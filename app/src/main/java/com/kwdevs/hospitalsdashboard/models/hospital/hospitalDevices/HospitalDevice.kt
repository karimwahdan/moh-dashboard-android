package com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class HospitalDevice(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "hospital_id")
    val hospitalId: Int?=null,

    @Json(name = "department_id")
    val departmentId: Int?=null,

    @Json(name = "device_type_id")
    val typeId: Int?=null,

    @Json(name = "name")
    val name: String,

    @Json(name = "code")
    val code: String? = null,

    @Json(name = "status_id")
    val statusId: Int,

    @Json(name = "status")
    val status: Status? = null,

    @Json(name = "created_by_id")
    val createdById: Int? = null,

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

    // --- Related Models (Optional) ---
    @Json(name = "hospital")
    val hospital: SimpleHospital? = null,

    @Json(name = "type")
    val type:DeviceType?=null,

    @Json(name = "department")
    val department: HospitalDepartment? = null,

    @Json(name = "usages")
    val usages:List<HospitalDeviceDailyUsage> = emptyList(),

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null,


    )
