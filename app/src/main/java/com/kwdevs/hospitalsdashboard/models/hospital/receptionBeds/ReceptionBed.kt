package com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class ReceptionBed(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "beds")
    val beds:Int?=null,

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

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null,
)
