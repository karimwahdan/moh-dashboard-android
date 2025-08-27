package com.kwdevs.hospitalsdashboard.models.hospital.clinics

import com.squareup.moshi.Json

data class DailyClinicVisitSingleResponse(
    @Json(name = "data")
    var data:DailyClinicVisit?
)
