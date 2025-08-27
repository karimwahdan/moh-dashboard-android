package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.squareup.moshi.Json

data class BloodBank(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "name")
    val name:String?=null,
    @Json(name = "blood_bank_type_id")
    val bloodBankTypeId:Int?=null,
    @Json(name = "type")
    val type:BasicModel?=null,
    @Json(name = "is_nbts")
    val isNationalBloodBank:Boolean?=null,
)
