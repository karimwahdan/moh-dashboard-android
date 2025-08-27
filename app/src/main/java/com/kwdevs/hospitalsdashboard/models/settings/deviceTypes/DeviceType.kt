package com.kwdevs.hospitalsdashboard.models.settings.deviceTypes

import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceDailyUsage
import com.squareup.moshi.Json

data class DeviceType(
    @Json(name = "id")
    val id:Int,
    @Json(name = "name")
    val name:String,
    @Json(name="usages")
    val usages:List<HospitalDeviceDailyUsage> = emptyList(),
)
