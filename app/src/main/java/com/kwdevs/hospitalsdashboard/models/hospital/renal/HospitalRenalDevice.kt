package com.kwdevs.hospitalsdashboard.models.hospital.renal

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class HospitalRenalDevice(
    @Json(name = "id")
    val id: Int?=null,

    @Json(name = "hospital_id")
    val hospitalId: Int? = null,

    @Json(name = "name")
    val name: String?=null,

    @Json(name = "renal_device_type_id")
    val typeId: Int? = null,

    @Json(name = "type")
    val type: DeviceType? = null,

    @Json(name = "active")
    val active: Boolean,

    @Json(name = "updated_by_id")
    val updatedById: Int? = null,

    @Json(name = "deleted_by_id")
    val deletedById: Int? = null,

    @Json(name = "updated_by_super_id")
    val updatedBySuperId: Int? = null,

    @Json(name = "updated_at_super")
    val updatedAtSuper: String? = null,

    @Json(name = "deleted_by_super_id")
    val deletedBySuperId: Int? = null,

    @Json(name = "deleted_at_user")
    val deletedAtUser: String? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    // Optional nested objects for relationships
    @Json(name = "hospital")
    val hospital: SimpleHospital? = null,

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null
)
// Nada => 25
// Allam => 25
// Ibrahim => 24
// Rafaat reg => 6
// Ahmed ali nurse 1 - 2 => 6
// Doha tech => 6