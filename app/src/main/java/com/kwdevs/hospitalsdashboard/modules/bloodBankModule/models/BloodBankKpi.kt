package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.squareup.moshi.Json

data class BloodBankKpi(
    @Json(name = "id") val id:Int?=null,
    @Json(name = "hospital_id") val hospitalId:Int?=null,
    @Json(name = "blood_bank_id") val bloodBankId:Int?=null,
    @Json(name = "department_id") val departmentId:Int?=null,
    @Json(name = "year") val year:String?=null,
    @Json(name = "month") val month:String?=null,

    @Json(name = "item_id") val itemId:Int?=null,
    @Json(name = "value") val value:Float?=null,
    @Json(name = "department") val department:BasicBloodBankDepartment?=null,
    @Json(name = "item") val item:BloodBankKpiItem?=null,
    @Json(name = "note") val note:String?=null,
)
