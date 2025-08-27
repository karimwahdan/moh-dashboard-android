package com.kwdevs.hospitalsdashboard.models.hospital.morgues

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.MorgueType
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class Morgue(
    @Json(name = "id")
    val id: Int?=null,

    @Json(name = "hospital_id")
    val hospitalId: Int?=null,

    @Json(name = "all_units")
    val allUnits: Int?=null,

    @Json(name = "free_units")
    val freeUnits: Int?=null,

    @Json(name = "status_id")
    val statusId: Int? = null,

    @Json(name = "morgue_type_id")
    val typeId:Int?=null,

    @Json(name = "status")
    val status: Status?=null,

    @Json(name = "type")
    val type:MorgueType?=null,

    @Json(name = "updated_by_id")
    val updatedBy: Int? = null,

    @Json(name = "deleted_by_id")
    val deletedById: Int? = null,

    @Json(name = "updated_by_super_id")
    val updatedBySuperId: Int? = null,

    @Json(name = "updated_at_super")
    val updatedAtSuper: String? = null,

    @Json(name = "deleted_by_super_id")
    val deletedBySuperId: Int? = null,

    @Json(name = "deleted_at_super")
    val deletedAtSuper: String? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    // Optional nested relationship if included in the API response
    @Json(name = "hospital")
    val hospital: SimpleHospital? = null,

    @Json(name = "updated_by")
    val updatedByUser: SimpleHospitalUser?=null,

    @Json(name = "updated_by_super")
    val updatedBySuperUser: SimpleSuperUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null
)
