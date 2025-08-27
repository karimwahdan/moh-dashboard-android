package com.kwdevs.hospitalsdashboard.models.hospital.clinics

import com.kwdevs.hospitalsdashboard.models.settings.generalClinics.GeneralClinic
import com.squareup.moshi.Json

data class HospitalClinic(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "general_clinic_id")
    val generalClinicId:Int?=null,
    @Json(name = "clinic")
    val generalClinic: GeneralClinic?=null,
    @Json(name = "visits")
    val visits:List<DailyClinicVisit> = emptyList(),
    @Json(name = "total_visits")
    val totalVisitsThisMonth:Int?=null,
)
