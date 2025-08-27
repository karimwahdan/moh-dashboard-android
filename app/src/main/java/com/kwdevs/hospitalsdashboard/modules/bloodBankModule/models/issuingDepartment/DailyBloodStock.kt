package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.squareup.moshi.Json

data class DailyBloodStock(
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

    @Json(name = "amount")
    val amount:Int?=null,

    @Json(name = "strategic")
    val emergency:Int?=null,

    @Json(name = "under_inspection")
    val underInspection:Boolean?=null,

    @Json(name = "blood_group")
    val bloodGroup: BasicModel?=null,

    @Json(name = "blood_unit_type")
    val bloodUnitType: BasicModel?=null,

    @Json(name = "created_at")
    val createdAt:String?=null,

    @Json(name = "entry_date")
    val entryDate:String?=null,

    @Json(name = "time_block")
    val timeBlock:String?=null,
    @Json(name = "hospital")
    val hospital:SimpleHospital?=null,
    @Json(name = "blood_bank")
    val bloodBank:BloodBank?=null,
    )
