package com.kwdevs.hospitalsdashboard.models.hospital

import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardType
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class HospitalWard(
    @Json(name = "id")
    val id: Int?=null,

    @Json(name = "hospital_id")
    val hospitalId: Int?=null,

    @Json(name = "name")
    val name: String,

    @Json(name = "ward_type")
    val type: WardType?=null,

    @Json(name = "all_units")
    val allBeds: Int?=null,

    @Json(name = "free_units")
    val freeBeds: Int?=null,

    @Json(name = "active")
    val active:Boolean,

    @Json(name = "admissions")
    val admissions:List<PatientAdmission> = emptyList(),

    // Optional nested objects if returned by the API
    @Json(name = "hospital")
    val hospital: SimpleHospital? = null,

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

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null
)
