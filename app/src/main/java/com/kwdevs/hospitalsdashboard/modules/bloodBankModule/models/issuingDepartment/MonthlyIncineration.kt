package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class MonthlyIncineration(
    @Json(name = "id")
    val id:Int?=null,

    @Json(name = "hospital_id")
    val hospitalId:Int?=null,

    @Json(name = "blood_bank_id")
    val bloodBankId:Int?=null,

    @Json(name = "blood_unit_type_id")
    val bloodUnitTypeId:Int?=null,

    @Json(name = "blood_group_id")
    val bloodGroupId:Int?=null,

    @Json(name = "value")
    val value:Int?=null,

    @Json(name = "month")
    val month:String?=null,

    @Json(name = "year")
    val year:String?=null,

    @Json(name = "incineration_reason_id")
    val reasonId:Int?=null,

    @Json(name = "blood_group")
    val bloodGroup: BasicModel?=null,

    @Json(name = "unit_type")
    val bloodUnitType: BasicModel?=null,

    @Json(name = "reason")
    val reason: IncinerationReason?=null,

    @Json(name = "created_at")
    val createdAt:String?=null,

    @Json(name = "department_id")
    val departmentId:Boolean?=null,

    @Json(name = "by_issuing")
    val byIssuing:Boolean?=null,

    @Json(name = "by_bcd")
    val byBCD:Boolean?=null,

    @Json(name = "department")
    val department:BloodBankDepartment?=null,

    @Json(name = "collection")
    val campaign: DailyBloodCollection?=null,


    )
