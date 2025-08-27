package com.kwdevs.hospitalsdashboard.models.settings.multipleReturns

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.squareup.moshi.Json

data class CrudDeviceResponse(
    @Json(name = "statuses")
    val statuses:List<Status> = emptyList(),
    @Json(name = "types")
    val types:List<DeviceType> = emptyList(),
    @Json(name = "departments")
    val departments:List<HospitalDepartment> = emptyList(),
)
