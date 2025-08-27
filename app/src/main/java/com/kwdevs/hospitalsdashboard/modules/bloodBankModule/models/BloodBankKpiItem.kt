package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.squareup.moshi.Json

data class BloodBankKpiItem(
    @Json(name = "id") val id:Int?=null,
    @Json(name = "name") val name:String?=null,
    @Json(name = "department_id") val departmentId:Int?=null,
    @Json(name = "description") val description:String?=null,
    @Json(name = "target") val target:Float?=null,
    @Json(name = "department") val department:BasicBloodBankDepartment?=null,

)
