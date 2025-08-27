package com.kwdevs.hospitalsdashboard.models.hospital.renal

import com.squareup.moshi.Json

data class RenalWashFrequency(
    @Json(name = "id")
    val id:Int?=null,

    @Json(name = "hospital_id")
    val hospitalId:Int?=null,

    @Json(name = "device_id")
    val deviceId:Int?=null,

    @Json(name = "device")
    val device:HospitalRenalDevice?=null,

    @Json(name = "day")
    val day:String?=null,

    @Json(name = "sessions")
    val sessions:Int?=null,
)
