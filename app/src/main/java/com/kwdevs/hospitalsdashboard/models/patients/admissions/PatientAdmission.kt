package com.kwdevs.hospitalsdashboard.models.patients.admissions

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardType
import com.squareup.moshi.Json

data class PatientAdmission(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "patient_id")
    val patientId:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "admission_date")
    val admissionTime:String?=null,
    @Json(name = "exit_date")
    val exitTime: String?=null,
    @Json(name = "ward_id")
    val wardId: Int?=null,
    @Json(name = "ward_type_id")
    val wardTypeId: Int?=null,
    @Json(name = "patient_state_id")
    val patientStateId:Int?=null,
    @Json(name = "bed_code")
    val bedCode:String?=null,
    @Json(name = "patient_quit")
    val patientQuit: Boolean?=null,
    @Json(name = "patient_die")
    val patientDie:Boolean?=null,

    @Json(name = "patient")
    val patient: Patient?=null,
    @Json(name = "hospital")
    val hospital:SimpleHospital?=null,

    @Json(name = "ward_type")
    val wardType: WardType?=null,

    @Json(name = "ward")
    val ward:HospitalWard?=null,

    @Json(name = "patient_state")
    val patientState: PatientState?=null

)
