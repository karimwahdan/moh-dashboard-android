package com.kwdevs.hospitalsdashboard.models.patients.babyBirth

import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.settings.BirthType
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class BabyBirth(
    @Json(name = "id")
    val id:Int?=null,

    @Json(name = "hospital_id")
    val hospitalId:Int?=null,

    @Json(name = "birthdate")
    val birthdate:String?=null,

    @Json(name = "mother_id")
    val motherId:Int?=null,

    @Json(name = "mother")
    val mother:Patient?=null,

    @Json(name = "birth_type_id")
    val birthTypeId:Int?=null,

    @Json(name = "birth_type")
    val birthType:BirthType?=null,

    @Json(name = "baby_died")
    val babyDied:Boolean?=null,

    @Json(name = "mother_died")
    val motherDied:Boolean?=null,

    @Json(name = "created_by_id")
    val createdById: Int? = null,

    @Json(name = "updated_by_id")
    val updatedById: Int? = null,

    @Json(name = "deleted_by_id")
    val deletedById: Int? = null,

    @Json(name = "created_by_super_id")
    val createdBySuperId: Int? = null,

    @Json(name = "updated_by_super_id")
    val updatedBySuperId: Int? = null,

    @Json(name = "updated_at_super")
    val updatedAtSuper: String? = null,

    @Json(name = "deleted_by_super_id")
    val deletedBySuperId: Int? = null,

    @Json(name = "deleted_at_user")
    val deletedAtUser: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "created_by")
    val createdBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "created_by_super")
    val createdBySuper: SimpleSuperUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null

)
