package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.squareup.moshi.Json

data class BloodImport(
    @Json(name = "id") val id:Int?=null,

    @Json(name = "hospital_id") val hospitalId:Int?=null,
    @Json(name = "blood_bank_id") val bloodBankId:Int?=null,

    @Json(name = "unit_type_id") val unitTypeId:Int?=null,
    @Json(name = "blood_group_id") val bloodGroupId:Int?=null,
    @Json(name = "quantity") val quantity:Int?=null,

    @Json(name = "by_patient") val byPatient:Boolean?=null,
    @Json(name = "is_private_sector") val isPrivateSector:Boolean?=null,
    @Json(name = "is_gov") val isGov:Boolean?=null,
    @Json(name = "is_nbts") val isNbts:Boolean?=null,

    @Json(name = "day") val day:String?=null,
    @Json(name = "month") val month:String?=null,
    @Json(name = "year") val year:String?=null,

    @Json(name = "blood_group") val bloodGroup: BasicModel?=null,
    @Json(name = "unit_type") val bloodUnitType: BasicModel?=null,

    @Json(name = "hospital_name") val hospitalName:String?=null,
    @Json(name = "hospital") val hospital:SimpleHospital?=null,
    @Json(name = "blood_bank") val bloodBank:BloodBank?=null,
    @Json(name = "sender_hospital") val senderHospital:SimpleHospital?=null,

    @Json(name = "created_by") val createdAt:String?=null,
)