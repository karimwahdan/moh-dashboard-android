package com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class HospitalDeviceUsagesResponse(
    @Json(name = "data")
    var data:List<HospitalDeviceDailyUsage> = emptyList()
)
