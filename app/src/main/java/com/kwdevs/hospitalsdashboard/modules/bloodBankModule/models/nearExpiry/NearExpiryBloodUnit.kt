package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.nearExpiry

import com.squareup.moshi.Json

data class NearExpiryBloodUnit(
    @Json(name = "id") val id:Int?=null,
    @Json(name = "hospital_id") val hospitalId:Int?=null,
    @Json(name = "blood_bank_id") val bloodBankId:Int?=null,
    @Json(name = "unit_type_id") val unitTypeId:Int?=null,
    @Json(name = "blood_group_id") val bloodGroupId:Int?=null,
    @Json(name = "status_id") val statusId:Int?=null,
    @Json(name = "quantity") val quantity:Int?=null,
    @Json(name = "code") val code:Int?=null,
    @Json(name = "expiry_date") val expiryDate:Int?=null,
    @Json(name = "recipient_type_id") val recipientTypeId:Int?=null,
    @Json(name = "recipient_id") val recipientId:Int?=null,
    @Json(name = "recipient_name") val recipientName:Int?=null,
    @Json(name = "recipient_national_id") val recipientNationalId:Int?=null,

    )
