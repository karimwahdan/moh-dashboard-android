package com.kwdevs.hospitalsdashboard.models.settings.statuses

import com.squareup.moshi.Json

data class StatusResponse(
    @Json(name = "data")
    var data:List<Status>
)
