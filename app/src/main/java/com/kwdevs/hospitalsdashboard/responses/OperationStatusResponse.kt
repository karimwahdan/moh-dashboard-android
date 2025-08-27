package com.kwdevs.hospitalsdashboard.responses

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationStatus
import com.squareup.moshi.Json

data class OperationStatusResponse(
    @Json(name = "data")
    var data:List<OperationStatus>
)
