package com.kwdevs.hospitalsdashboard.models.patients.admissions

import com.squareup.moshi.Json

data class PatientAdmissionSingleResponse(
    @Json(name = "data")
    val data: PatientAdmission
)
