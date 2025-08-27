package com.kwdevs.hospitalsdashboard.responses.options

import com.kwdevs.hospitalsdashboard.models.settings.MorgueType
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.squareup.moshi.Json

data class MorgueOptionsResponse(
    @Json(name = "statuses")
    val statuses:List<Status> = emptyList(),
    @Json(name = "types")
    val types:List<MorgueType> = emptyList(),
)
