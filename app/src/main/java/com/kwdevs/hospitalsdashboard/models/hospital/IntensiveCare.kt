package com.kwdevs.hospitalsdashboard.models.hospital

import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class IntensiveCare(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId: Int?=null,

    @Json(name = "has_icu")
    val hasIcu: Boolean?,

    @Json(name = "all_icu_beds")
    val allIcuBeds: Int?,

    @Json(name = "free_icu_beds")
    val freeIcuBeds: Int?,

    @Json(name = "has_ccu")
    val hasCcu: Boolean?,

    @Json(name = "all_ccu_beds")
    val allCcuBeds: Int?,

    @Json(name = "free_ccu_beds")
    val freeCcuBeds: Int?,

    @Json(name = "has_neuro_cu")
    val hasNeuroCu: Boolean?,

    @Json(name = "all_neuro_cu_beds")
    val allNeurologyCareUnitBeds: Int?,

    @Json(name = "free_neuro_cu_beds")
    val freeNeurologyCareUnitBeds: Int?,

    @Json(name = "has_nicu")
    val hasNicu: Boolean?,

    @Json(name = "all_nicu_beds")
    val allNicuBeds: Int?,

    @Json(name = "free_nicu_beds")
    val freeNicuBeds: Int?,

    @Json(name = "has_burn_cu")
    val hasBurnCareUnit: Boolean?,

    @Json(name = "all_burn_cu_beds")
    val allBurnCareUnitBeds: Int?,

    @Json(name = "free_burn_cu_beds")
    val freeBurnCareUnitBeds: Int?,

    @Json(name = "has_onco_cu")
    val hasOncologyCareUnit: Boolean?,

    @Json(name = "all_onco_cu_beds")
    val allOncologyCareUnitBeds: Int?,

    @Json(name = "free_onco_cu_beds")
    val freeOncologyCareUnitBeds: Int?,

    @Json(name = "hospital")
    var hospital:SimpleHospital? = null,


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
