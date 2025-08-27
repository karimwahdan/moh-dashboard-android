package com.kwdevs.hospitalsdashboard.models.users.normal

import com.squareup.moshi.Json

data class HospitalUserSingleResponse(
    @Json(name = "data")
    val data:HospitalUser,
)
