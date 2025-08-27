package com.kwdevs.hospitalsdashboard.models.hospital.operationRoom

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.squareup.moshi.Json

data class OperationRoomSingleResponse(
    @Json(name = "data")
    var data:OperationRoom? = null
)
