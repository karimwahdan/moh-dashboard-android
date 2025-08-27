package com.kwdevs.hospitalsdashboard.models.hospital.reception

import com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds.ReceptionBed
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.squareup.moshi.Json

data class ReceptionData(
    @Json(name = "bed")
    val receptionBedCount:ReceptionBed,
    @Json(name = "frequencies")
    val admissions:PaginationData<ReceptionFrequency>
)
