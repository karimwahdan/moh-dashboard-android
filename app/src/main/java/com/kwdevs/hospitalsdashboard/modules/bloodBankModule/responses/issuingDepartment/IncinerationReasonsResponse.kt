package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.squareup.moshi.Json

data class IncinerationReasonsResponse(
    @Json(name = "data")
    val data: List<IncinerationReason> = emptyList(),
)
