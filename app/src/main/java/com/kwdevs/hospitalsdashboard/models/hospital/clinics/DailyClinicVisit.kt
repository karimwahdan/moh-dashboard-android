package com.kwdevs.hospitalsdashboard.models.hospital.clinics

import com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes.ClinicVisitType
import com.squareup.moshi.Json

data class DailyClinicVisit(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "clinic_id")
    val clinicId:Int?=null,
    @Json(name = "day")
    val day:String?=null,
    @Json(name = "visits")
    val visits:Int?=null,
    @Json(name = "visit_type_id")
    val visitTypeId:Int?=null,
    @Json(name = "visit_type")
    val visitType:ClinicVisitType?=null,

)
