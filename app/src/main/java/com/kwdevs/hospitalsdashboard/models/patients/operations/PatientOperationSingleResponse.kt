package com.kwdevs.hospitalsdashboard.models.patients.operations

import com.squareup.moshi.Json

data class PatientOperationSingleResponse(
    @Json(name = "data")
    val data: PatientOperation
)
