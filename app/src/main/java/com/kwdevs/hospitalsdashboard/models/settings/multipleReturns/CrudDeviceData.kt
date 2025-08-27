package com.kwdevs.hospitalsdashboard.models.settings.multipleReturns

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.squareup.moshi.Json

data class CrudDeviceData(
    @Json(name = "data")
    val data:CrudDeviceResponse,
)
