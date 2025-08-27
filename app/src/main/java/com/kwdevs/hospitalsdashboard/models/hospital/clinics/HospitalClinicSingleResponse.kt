package com.kwdevs.hospitalsdashboard.models.hospital.clinics

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class HospitalClinicSingleResponse(
    @Json(name = "data")
    var data:HospitalClinic?
)
