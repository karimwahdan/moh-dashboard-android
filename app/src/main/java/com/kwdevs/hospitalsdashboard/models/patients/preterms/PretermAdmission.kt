package com.kwdevs.hospitalsdashboard.models.patients.preterms

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.squareup.moshi.Json

data class PretermAdmission(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "mother_id")
    val motherId:Int?=null,
    @Json(name = "patient_id")
    val patientId:Int?=null,
    @Json(name = "patient_code_id")
    val patientCodeId: Int?=null,
    @Json(name = "admission_date")
    val admissionTime:String?=null,
    @Json(name = "exit_date")
    val exitTime: String?=null,
    @Json(name = "patient_quit")
    val patientQuit: Boolean?=null,
    @Json(name = "patient_die")
    val patientDie:Boolean?=null,

    @Json(name = "patient_state_id")
    val patientStateId:Int?=null,

    @Json(name = "mother")
    val mother: Patient?=null,
    @Json(name = "patient")
    val patient: Patient?=null,
    @Json(name = "patient_state")
    val patientState: PatientState?=null,
    @Json(name = "hospital")
    val hospital:SimpleHospital?=null,

)
