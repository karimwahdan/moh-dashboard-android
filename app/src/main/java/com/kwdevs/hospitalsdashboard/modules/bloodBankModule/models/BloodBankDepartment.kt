package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.squareup.moshi.Json

data class BloodBankDepartment(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "department_id")
    val departmentId:Int?=null,
    @Json(name = "department")
    var department:BasicBloodBankDepartment?=null,
)
