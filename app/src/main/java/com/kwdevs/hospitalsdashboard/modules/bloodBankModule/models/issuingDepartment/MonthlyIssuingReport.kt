package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.squareup.moshi.Json

data class MonthlyIssuingReport(
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

    @Json(name = "quantity")
    val quantity:Int?=null,

    @Json(name = "month")
    val month:String?=null,

    @Json(name = "year")
    val year:String?=null,

    @Json(name = "is_inpatient")
    val isInPatient:Boolean?=null,

    @Json(name = "is_outpatient")
    val isOutPatient:Boolean?=null,

    @Json(name = "is_private_sector")
    val isPrivateSector:Boolean?=null,

    @Json(name = "is_hospital")
    val isHospital:Boolean?=null,

    @Json(name = "is_national_blood_bank")
    val isNationalBloodBank:Boolean?=null,

    @Json(name = "receiving_blood_bank_id")
    val receivingBloodBankId:Int?=null,

    @Json(name = "receiving_hospital_id")
    val receivingHospitalId:Int?=null,

    @Json(name = "hospital_name")
    val hospitalName:String?=null,

    @Json(name = "receiving_hospital")
    val receivingHospital: SimpleHospital?=null,


    @Json(name = "receiving_blood_bank")
    val receivingBloodBank: SimpleHospital?=null,

    @Json(name = "blood_group")
    val bloodGroup: BasicModel?=null,

    @Json(name = "unit_type")
    val bloodUnitType: BasicModel?=null,

    @Json(name = "reason")
    val reason: IncinerationReason?=null,

    @Json(name = "created_at")
    val createdAt:String?=null,

    )
