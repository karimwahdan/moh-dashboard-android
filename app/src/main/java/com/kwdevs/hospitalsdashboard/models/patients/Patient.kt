package com.kwdevs.hospitalsdashboard.models.patients

import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.squareup.moshi.Json

data class Patient(
    @Json(name = "id")
    val id:Int?=null,

    @Json(name = "first_name")
    val firstName:String?=null,

    @Json(name = "second_name")
    val secondName:String?=null,

    @Json(name = "third_name")
    val thirdName:String?=null,

    @Json(name = "fourth_name")
    val fourthName:String?=null,

    @Json(name = "nationality_id")
    val nationalityId:Int?=null,

    @Json(name = "national_id")
    val nationalId:String?=null,

    @Json(name = "mobile_no")
    val mobileNumber:String?=null,

    @Json(name = "alt_mobile_no")
    val alternativeMobileNumber:String?=null,

    @Json(name = "addresses")
    val addresses:List<PatientAddress> = emptyList(),

    @Json(name = "admissions")
    val admissions:List<PatientAdmission> = emptyList(),

    @Json(name = "nationality")
    val nationality: Nationality?=null,

    @Json(name = "patient_code")
    val patientCode:PatientCode?=null,

    @Json(name = "codes")
    val codes:List<PatientCode>?= emptyList(),

    @Json(name = "gender")
    val gender:Int?=null,

    @Json(name = "active")
    val active:Boolean?=null,

    @Json(name = "deleted_at")
    val deletedAt:String?=null,
)
