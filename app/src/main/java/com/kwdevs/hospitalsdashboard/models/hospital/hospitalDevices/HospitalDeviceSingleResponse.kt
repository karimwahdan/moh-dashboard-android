package com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.squareup.moshi.Json

data class HospitalDeviceSingleResponse(
    @Json(name = "data")
    var data:HospitalDevice?
)
