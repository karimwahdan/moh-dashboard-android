package com.kwdevs.hospitalsdashboard.models.hospital.clinics

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class HospitalClinicResponse(
    @Json(name = "data")
    var data:List<HospitalClinic> = emptyList()
)
