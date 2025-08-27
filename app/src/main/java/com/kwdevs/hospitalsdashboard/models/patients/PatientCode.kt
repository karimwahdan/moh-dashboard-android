package com.kwdevs.hospitalsdashboard.models.patients

import com.squareup.moshi.Json

data class PatientCode(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "patient_id")
    val patientId:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "code")
    val code:String?=null,
    @Json(name = "created_by_id")
    val createdById:Int?=null,
    @Json(name = "updated_by_id")
    val updatedById:Int?=null,
)
