package com.kwdevs.hospitalsdashboard.models.settings.deviceTypes

import com.squareup.moshi.Json

data class DeviceTypeResponse(
    @Json(name = "data")
    var data:List<DeviceType>
)
