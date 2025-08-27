package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.squareup.moshi.Json

data class BloodNearExpiredItem (
    @Json(name = "id") val id:Int?=null,
    @Json(name = "hospital_id") val hospitalId:Int?=null,
    @Json(name = "blood_bank_id") val bloodBankId:Int?=null,
    @Json(name = "unit_type_id") val unitTypeId:Int?=null,
    @Json(name = "blood_group_id") val bloodGroupId:Int?=null,
    @Json(name = "expiry_date") val expiryDate:String?=null,
    @Json(name = "code") val code:String?=null,
    @Json(name = "quantity") val quantity:Int?=null,
    @Json(name = "status_id") val statusId:Int?=null,
    @Json(name = "recipient_type_id") val recipientTypeId:Int?=null,
    @Json(name = "recipient_id") val recipientId:Int?=null,
    @Json(name = "recipient_name") val recipientName:String?=null,
    @Json(name = "created_by_id") val createdById:Int?=null,
    @Json(name = "updated_by_id") val updatedById:Int?=null,

    @Json(name = "hospital") val hospital:SimpleHospital?=null,
    @Json(name = "blood_bank") val bloodBank:BloodBank?=null,
    @Json(name = "unit_type") val unitType:BasicModel?=null,
    @Json(name = "status") val status:BasicModel?=null,

    @Json(name = "blood_group") val bloodGroup:BasicModel?=null,
    @Json(name = "recipient_type") val recipientType:BasicModel?=null,
    @Json(name = "recipient_hospital") val recipientHospital:SimpleHospital?=null,
)